# DMM Fast Key Binder
![Release](https://img.shields.io/badge/Version-Alpha%201.1-blue) ![Fabric](https://img.shields.io/badge/Loader-Fabric-cream) ![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4-green)

> **[🇰🇷 한국어 설명서 보기 (Korean Manual)](./README_KR.md)**

## Description
**DMM Fast Key Binder** is a lightweight command-binding utility for Minecraft client environments (Fabric 1.21.4).
It provides the functionality to map complex commands (e.g., `/gamemode creative`, `/time set day`) to specific keys (including combination keys) for instant execution.

It eliminates the hassle of opening the chat window and typing commands in third-party open servers or single-player environments, allowing you to easily manage macros through an **intuitive GUI**.

---

### Key Features
- **Unlimited Custom Bindings**: Register and manage an unlimited number of command macros with custom names.
- **Modifier Key Support (Modifiers)**: Supports various combination keys like Shift + F, Ctrl + Alt + R, ensuring no conflicts with existing keys.
- **Convenient GUI**: Change settings intuitively in-game using Mod Menu and Cloth Config.
- **Spam Prevention (Rising Edge)**: Designed so that commands are executed only once even if the key is held down.
- **Smart Input Correction**: Automatically recognizes and executes commands correctly whether you include the leading `/` or not.

---

<details>
<summary><strong>How to Use (Images) - Click to expand</strong></summary>

#### Set Config Shortcut
![Settings](ReadMeImage/1.png)
> In Minecraft's Key Binds settings, you can assign a shortcut to open the mod's dedicated configuration screen.

<br></br>

#### Add Binding
![Add Binding](ReadMeImage/2.png)
> Use the 'Add New' button to reveal the configuration area for new bindings in the list below.

<br></br>

#### Register Binding
![Register](ReadMeImage/3.png)
> Assign a name, enter the command, and click the key button to set your trigger key.  
> **Shift, Alt, and Ctrl** combination keys are supported.

<br></br>

#### Usage
![Usage](ReadMeImage/4.png)
> Press the bound key to instantly execute your command.

<br></br>

#### Duplicate Prevention
![Duplicate](ReadMeImage/5.png)
![Duplicate2](ReadMeImage/6.png)
> If you try to save a duplicate key binding, a **warning notification** will appear as shown above.  
> The key is still saved, so please check and manage your duplicate bindings carefully.

<br></br>

#### Vanilla Key Conflict
![Vanilla](ReadMeImage/7.png)
> If your key conflicts with a vanilla Minecraft key binding, the mod will inform you exactly which key it conflicts with.

**Enjoy Minecraft!**
</details>

---

### Build & Installation
- If you only downloaded the JAR file from Releases, please place it in the `.minecraft/mods` folder along with the required dependencies listed in step 4.
- Must be applied in a Fabric environment.

1. **Build**
   - Run `./gradlew build` in your terminal (or execute the `build` task in the IntelliJ Gradle tab).
   
2. **Verify File**
   - Check the `DMM_FastKeyBinder-xxx.jar` file in the `build/libs/` folder.
   - *Note: Files ending in `sources` or `dev` are not for distribution.*

3. **Install**
   - Copy the `.jar` file to the `mods` folder in your Minecraft installation path.

4. **Dependencies**
   - <Strong style="color:tomato">The following mods must be installed together for proper operation:</Strong>
     - [Fabric API](https://modrinth.com/mod/fabric-api) [1.21.4] Fabric API 0.119.4+1.21.4
     - [Cloth Config API](https://modrinth.com/mod/cloth-config) [Fabric 1.21.4] v17.0.142
     - [Mod Menu](https://modrinth.com/mod/modmenu) v13.0.2 for 1.21.4


### Deployment Roadmap

- Implementation of automated mod downloads using scripts.
