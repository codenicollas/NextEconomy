plugins {
    java
    id("com.gradleup.shadow") version "9.3.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "com.nextplugins"
version = "2.2.2"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()

    // spigot
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://oss.sonatype.org/content/groups/public/") }

    // libs
    maven { url = uri("https://jitpack.io/") }
    maven { url = uri("https://nexus.scarsz.me/content/groups/public/") }
    maven { url = uri("https://repo.codemc.io/repository/maven-public/") }
    maven { url = uri("https://repo.extendedclip.com/releases/") }
    maven { url = uri("https://repo.nickuc.com/maven-releases/") }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.11-R0.1-SNAPSHOT")

    implementation("com.github.HenryFabio.configuration-injector:bukkit:1.0.2")
    implementation("com.github.HenryFabio:sql-provider:9561f20fd2")
    implementation("com.github.SaiintBrisson.command-framework:bukkit:1.3.1")
    implementation("de.tr7zw:item-nbt-api:2.15.5")

    implementation("io.github.juliarn:npc-lib-api:3.0.0-beta11")
    implementation("io.github.juliarn:npc-lib-bukkit:3.0.0-beta11")
    implementation("io.github.juliarn:npc-lib-common:3.0.0-beta11")

    implementation("com.github.ben-manes.caffeine:caffeine:2.9.3") // 3.0.0 ou superior é para Java 11+
    implementation("org.apache.commons:commons-lang3:3.17.0")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    compileOnly("com.github.decentsoftware-eu:decentholograms:2.8.15")
    compileOnly("com.nickuc.chat:nchat-api:5.6")
    compileOnly("me.clip:placeholderapi:2.11.7")
    compileOnly("me.filoghost.holographicdisplays:holographicdisplays-api:3.0.0")

    compileOnly(fileTree("libs"))
    implementation(fileTree("libs") { include("inventory-api-2.0.4-FINAL.jar") })

    val lombok = "org.projectlombok:lombok:1.18.42"
    compileOnly(lombok)
    annotationProcessor(lombok)
}

bukkit {
    main = "com.nextplugins.economy.NextEconomy"
    authors = listOf("NextPlugins")
    website = "https://nextplugins.com.br"
    version = "${project.version}"
    depend = listOf("Vault")
    apiVersion = "1.13"
    load = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder.STARTUP
    softDepend = listOf(
        "DecentHolograms",
        "ProtocolLib",
        "NextTestServer",
        "CMI",
        "SkinsRestorer",
        "HolographicDisplays",
        "PlaceholderAPI",
        "Legendchat",
        "UltimateChat",
        "nChat",
        "DiscordSRV"
    )
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveBaseName.set(project.name)
        archiveClassifier.set("")

        val libsPackage = "${project.group}.libs"

        relocate("com.github.benmanes.caffeine", "$libsPackage.caffeine")
        relocate("com.google.errorprone.annotations", "$libsPackage.annotations")

        relocate("com.henryfabio.minecraft.configinjector", "$libsPackage.configinjector")
        relocate("com.henryfabio.minecraft.inventoryapi", "$libsPackage.inventoryapi")
        relocate("com.henryfabio.sqlprovider", "$libsPackage.sqlprovider")

        relocate("com.yuhtin.updatechecker", "$libsPackage.updatechecker")

        relocate("com.zaxxer.hikari", "$libsPackage.hikari")

        relocate("de.tr7zw.annotations", "$libsPackage.annotations")
        relocate("de.tr7zw.changeme.nbtapi", "$libsPackage.nbtapi")

        relocate("io.github.juliarn.npc", "$libsPackage.npclib")

        relocate("me.saiintbrisson.bukkit.command", "$libsPackage.command.bukkit")
        relocate("me.saiintbrisson.minecraft.command", "$libsPackage.command.common")
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(8)
    }
}