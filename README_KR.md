# DMM Fast Key Binder
![Release](https://img.shields.io/badge/Version-Alpha%201.1-blue) ![Fabric](https://img.shields.io/badge/Loader-Fabric-cream) ![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4-green)

> **[English Manual (영어 매뉴얼)](./README.md)**

## 설명 (Description)
**DMM Fast Key Binder**는 마인크래프트 클라이언트 환경(Fabric 1.21.4)에서 동작하는 **경량화된 키 바인딩 유틸리티**입니다.
복잡한 명령어(예: `/gamemode creative`, `/time set day`)를 자주 입력해야 하는 사용자를 위해, 원하는 키(조합 키 포함)에 명령어를 매핑하여 즉시 실행할 수 있는 기능을 제공합니다.

제 3자 오픈 서버나 싱글 플레이 환경에서 채팅창을 열고 타이핑하는 번거로움을 없애고, **직관적인 GUI**를 통해 손쉽게 매크로를 관리할 수 있습니다.

---

### 주요 기능
- **무제한 커스텀 바인딩**: 이름과 명령어를 제한 없이 등록하고 관리할 수 있습니다.
- **복합 키 지원 (Modifiers)**: Shift + F, Ctrl + Alt + R 등 다양한 조합 키를 지원하여 기존 키와 겹치지 않게 설정 가능합니다.
- **편리한 GUI**: Mod Menu와 Cloth Config를 통해 게임 내에서 설정을 직관적으로 변경합니다.
- **스팸 방지 (Rising Edge)**: 키를 꾹 누르고 있어도 명령어가 한 번만 실행되도록 설계되었습니다.
- **스마트 입력 보정**: 명령어 입력 시 앞의 /를 붙이거나 떼어도 자동으로 인식하여 올바르게 실행합니다.

---

<details>
<summary><strong>사용방법 (이미지) 보기</strong></summary>

해당 이미지는 언어가 영어인 사진입니다. 언어를 한국어로 해두시면 한글패치가 적용됩니다.

#### 설정창 단축키 설정
![설정창](ReadMeImage/1.png)
> 마인크래프트의 키 설정에 들어가시면 다음과 같이 모드 전용 설정창을 여는 단축키를 설정 가능합니다.

<br></br>

#### 바인딩 추가
![바인딩 추가](ReadMeImage/2.png)
> 새로 추가 버튼을 통해 아래 리스트에 바인딩을 추가할 수 있는 영역이 나옵니다.

<br></br>


#### 바인딩 등록
![여러개](ReadMeImage/3.png)
> 이름은 원하시는 이름으로 지어주시고, 커맨드에는 명령어를, 작동 키는 클릭하셔서 원하는 키를 등록해주세요.  
> **Shift, Alt, Ctrl**을 이용한 복합키를 사용 가능합니다.

<br></br>

#### 사용 이미지
![사용](ReadMeImage/4.png)
> 바인딩된 키를 눌러 원하는 명령어를 즉각적으로 실행할 수 있습니다.

<br></br>

#### 중복 방지
![중복](ReadMeImage/5.png)
![중복2](ReadMeImage/6.png)
> 중복된 키 바인딩을 설정하고 저장을 누르면 위와 같이 **중복키에 대한 경고 알림**이 나타납니다.  
> 키는 그대로 저장되므로 경고를 확인하여 중복된 키를 관리하시길 바랍니다.

<br></br>
#### 바닐라 키 중복 방지
![바닐라](ReadMeImage/7.png)
> 마인크래프트 기본 설정(바닐라) 키와 중복될 경우에도 어떤 키와 충돌하는지 상세히 알려드립니다.

**즐거운 마인크래프트 되시길 바랍니다.**
</details>

---

### 빌드 및 설치 방법
- Release에서 JAR파일만 다운로드 받으신 분들은 .minecraft/mods 파일내부에 4번 필수 모드들과 함께 넣어주시길 바랍니다.
- 반드시 Fabric 환경에서 적용하셔야 합니다.

1. **빌드 (Build)**
   - 터미널에서 ./gradlew build 실행 (또는 IntelliJ Gradle 탭에서 build 태스크 실행)
   
2. **파일 확인**
   - build/libs/ 폴더 내의 DMM_FastKeyBinder-xxx.jar 파일 확인
   - *주의: sources 또는 dev가 붙은 파일은 배포용이 아닙니다.*

3. **설치 (Install)**
   - 마인크래프트 설치 경로의 mods 폴더에 .jar 파일을 복사합니다.

4. **필수 의존성 (Dependencies)**
   - <Strong style="color:tomato">다음 모드들이 함께 설치되어 있어야 정상 작동합니다</Strong>
     - [Fabric API](https://modrinth.com/mod/fabric-api) [1.21.4] Fabric API 0.119.4+1.21.4
     - [Cloth Config API](https://modrinth.com/mod/cloth-config) [Fabric 1.21.4] v17.0.142
     - [Mod Menu](https://modrinth.com/mod/modmenu) v13.0.2 for 1.21.4


### 배포 편의성 개선안

-  스크립트를 활용하는 자동 모드 다운로드 구현
