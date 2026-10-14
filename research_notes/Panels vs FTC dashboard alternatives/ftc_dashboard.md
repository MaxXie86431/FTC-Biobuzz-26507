# FTC Dashboard (ACME Robotics) as an alternative to Panels, as of 14 Oct 2026

Context: Biobuzz 26507's current build files (read locally on 2026-10-14) show `com.bylazar:fullpanels:1.0.13` (TeamCode/build.gradle), `dev.nextftc.v2:{control,hardware,robot}:0.2.1`, `com.pedropathing:revhub:3.0.0`, `com.pedropathing:tuning:1.0.0`, `com.pedropathing:telemetry:1.0.0`, `com.pedropathing.ivy:pedro:1.1.1` and `dev.frozenmilk.sinister:Sloth:0.3.2` (build.dependencies.gradle; the comment there says Sloth is pinned to match Pedro tuning's runtime). Panels is used in only 3 Java files (`mechanisms/Intake.java`, `mechanisms/Intermediate.java`, `auto/pathing/Poses.java`), with 2 Panels references each. These are local observations, not web sources.

## 1. Latest release, activity in 2025-2026, maintenance status, maintainers

### Takeaway
The latest FTC Dashboard release is **v0.6.0 (16 May 2026)**. The project is actively developed rather than in maintenance mode: commits landed through 7 Sep 2026, and community PRs were opened as recently as 8 Oct 2026. Releases come from a single maintainer, Ryan Brott (rbrott), who merges in batches. Most 2025-26 feature work came from community contributors, and 21 PRs are waiting on merges.

### Cited Findings
- The latest release is v0.6.0, marked "Latest" and published by rbrott on 16 May. Its notes cover persisting graph variable selections across op mode runs, an OpModeManager error view, numeric input arrows, the update to SDK v11, a Robot Configuration Editor, color sensors in the hardware view, config variable highlighting, a Limelight 3A proxy and view, and a Gamepad view — [GitHub releases](https://github.com/acmerobotics/ftc-dashboard/releases)
- Earlier releases, all published by rbrott: v0.5.1 (31 Oct 2025: CRServo hardware-view crash fix, a fix for infinite recursion into static members, op mode groups in the selector, Kotlin support, guide button); v0.5.0 (9 Sep 2025: DECODE field image, hardware view, standard gamepad mapping); v0.4.17 (1 Jul 2025: SDK v10.3, Limelight 3A streaming, RecorderView for field replays, "copy as Java" for config, DualSense support); v0.4.16 (9 Sep 2024: INTO THE DEEP field, SDK v10.0) — [GitHub releases](https://github.com/acmerobotics/ftc-dashboard/releases); years confirmed from [commit history](https://github.com/acmerobotics/ftc-dashboard/commits/master)
- The Maven metadata lists `com.acmerobotics.dashboard:dashboard` with latest = release = **0.6.0**, lastUpdated `20260516173312` (16 May 2026), and 39 versions from 0.1.0 to 0.6.0. The repository is `https://maven.brott.dev/`, which redirects to an Azure blob host — [maven-metadata.xml](https://acmerobotics.z13.web.core.windows.net/com/acmerobotics/dashboard/dashboard/maven-metadata.xml)
- 2026 commits on master:
  - 22 Mar 2026: Limelight 3A proxy (#215, mxtmx) and config variable highlighting (#161)
  - 16 May 2026: Gamepad View (#214) and the v0.6.0 release
  - 22 Aug 2026: "Prevent keyboard gamepad from picking up edit events"
  - 7 Sep 2026: five rbrott commits ("Fix lints and add CI enforcement", "Add nix flake", "Fix TS lints", "Remove button outline on selection", "Add task to easily run the test server through Gradle"). These are the most recent commits on master as of 14 Oct 2026.
  - Late 2025 commits include "Update to SDK v11 (#212)" (24 Dec 2025), the robot configuration editor (#189, 24 Dec 2025), and the error view (#201, 6 Dec 2025).

  Source: [commit history](https://github.com/acmerobotics/ftc-dashboard/commits/master)
- Contributors in 2025-2026 besides rbrott include mxtmx, GraysonTinker (WHS Robotics), ericwooshem, zachwaffle4, Vlad1-1, bubner, qwertychouskie, monkbroc and aaditn — [commit history](https://github.com/acmerobotics/ftc-dashboard/commits/master)
- There are 21 open PRs, 17 of them opened between 22 Jul and 8 Oct 2026. Examples: #237 deliver dashboard gamepads to iterative op modes (8 Oct 2026), #236 enum config null fix (29 Sep 2026), #233 Loop Time view (22 Sep 2026), #231 allow disabling the Limelight proxy (18 Sep 2026), #229 share/save custom layouts, #228 scrollable and replayable graphs, #227 record and replay Telemetry/Graph/Field/Logging (3 Sep 2026), #226 telemetry search filter, #225 graph markers, #218 LogView (export Control Hub logs). Older PRs are still unmerged, for example #134, a visual Road Runner path editor (Feb 2023) — [open PRs](https://github.com/acmerobotics/ftc-dashboard/pulls)
- Repo stats: 218 stars, 200 forks, 23 open issues, 21 open PRs — [GitHub repo](https://github.com/acmerobotics/ftc-dashboard)
- rbrott is Ryan Brott, who also authors Road Runner. The Road Runner quickstart's last master commit is "Bump dash" (31 Oct 2025), and it shows no SDK v11.1, v11.2 or v12 merges — [road-runner-quickstart commits API](https://api.github.com/repos/acmerobotics/road-runner-quickstart/commits?per_page=8)

### Inferences
- FTC Dashboard is maintained, with release-level work roughly two to three times a year. Throughput depends on one gatekeeper (rbrott): a large backlog of fall-2026 community PRs is waiting for the next release, and there has been no tagged release since May 2026.
- The ACME Road Runner ecosystem (the quickstart) looks dormant in 2026, while the dashboard itself still gets community-driven features.

### Gaps
- There is no public roadmap or statement of maintenance status. The README does not describe one.
- No date is announced for a v0.6.1 or v0.7.0 that would merge the Sep-Oct 2026 PRs.

## 2. Features, and what it lacks compared with Panels (and vice versa)

### Takeaway
FTC Dashboard covers the core needs of a Pedro or NextFTC team: `@Config` live variables, telemetry with graphing, a Canvas field overlay, camera streaming, limited OpMode control and gamepads. Since 2025 it has also added hardware and config views, a Limelight proxy, error views and replay. Compared with Panels, it has no plugin system, no built-in Pedro coordinate preset, and no 2026-27 field image yet. Several Panels-like features (record/replay, loop-time view, log export) are still unmerged PRs.

### Cited Findings
- Core features listed in the README: live telemetry with plots and field graphics, live configuration variables, camera streaming, limited op mode controls and gamepad support ("volatile due to unstable browser APIs"), custom dashboard layouts, and telemetry CSV export — [GitHub README](https://github.com/acmerobotics/ftc-dashboard)
- Telemetry: `TelemetryPacket.put()`, `packet.fieldOverlay()` (returns a `Canvas`), `FtcDashboard.getInstance().sendTelemetryPacket(packet)`, and `getTelemetry()`. The docs warn that the `update()` indirection "can mask the presence of multiple `sendTelemetryPacket()` calls in a single loop iteration". `MultipleTelemetry` writes to the Driver Station and the dashboard in one `update()` — [Features docs](https://acmerobotics.github.io/ftc-dashboard/features)
- Config variables: "declare it `static` and not `final` and annotate the enclosing class with `@Config`". Changes saved in the dashboard apply to fields immediately, but code-side changes reach the client only on an explicit refresh. Values copied into instance fields go stale, so the docs recommend `volatile`. In Kotlin, use an `object` with `@JvmField var` — [Features docs](https://acmerobotics.github.io/ftc-dashboard/features)
- Config variables also support arrays (v0.4.14), basic long and float values (v0.4.15), "copy as Java" (v0.4.17) and Kotlin (v0.5.1) — [GitHub releases](https://github.com/acmerobotics/ftc-dashboard/releases)
- Camera: `FtcDashboard.getInstance().startCameraStream(camera, 0)` takes any `CameraStreamSource` (EasyOpenCV or VisionPortal), and Limelight 3A streaming is supported (v0.4.17) — [Features docs](https://acmerobotics.github.io/ftc-dashboard/features); [releases](https://github.com/acmerobotics/ftc-dashboard/releases)
- OpMode control and gamepads: the docs say dashboard gamepads have "higher latency and less robustness" than the Driver Station and that disconnect safety has "no guarantees". Issue #131 "Gamepad support broken on all browsers now?" (opened Feb 2023) is still open, although v0.5.0 added standard gamepad mapping and v0.6.0 added a Gamepad view — [Features docs](https://acmerobotics.github.io/ftc-dashboard/features); [issues](https://github.com/acmerobotics/ftc-dashboard/issues)
- Field drawing: v0.4.11 added text, image and grid primitives plus coordinate transformations to the Canvas. Field images ship per season, and v0.5.0 (Sep 2025) added DECODE and removed old season images. A Nov 2025 docs commit explains how to add custom images (#208) — [releases](https://github.com/acmerobotics/ftc-dashboard/releases); [commits](https://github.com/acmerobotics/ftc-dashboard/commits/master)
- Newer views: hardware view (v0.5.0), robot configuration editor, color sensors in the hardware view, OpModeManager error view, Limelight 3A proxy and view, and Gamepad view (all v0.6.0) — [releases](https://github.com/acmerobotics/ftc-dashboard/releases)
- Pending PRs, not in any release as of 14 Oct 2026: a Loop Time view (#233), record and replay of Telemetry/Graph/Field/Logging (#227), shareable layouts (#229), graph markers and tooltips (#225, #222), a Control Hub log export (#218), and a binary struct telemetry encoding (#196) — [open PRs](https://github.com/acmerobotics/ftc-dashboard/pulls)
- Open feature requests include "Dashboard plugins" (#184, Aug 2025), a composable custom widget API (#127), and displaying user code exceptions (#153) — [issues](https://github.com/acmerobotics/ftc-dashboard/issues)
- Panels describes itself as an "all-in-one dashboard" that lets you control OpModes, tune Limelight pipelines wirelessly, record and replay match data, configure variables in real time, visualize telemetry with interactive field and graph tools, and "build plugins for any feature". Its headline is "Panels 1.0: Built on Plugins" — [ftcontrol-panels GitHub](https://github.com/ftcontrol/ftcontrol-panels)
- Panels' field view has a Pedro Pathing offset preset that rotates and shifts the canvas to Pedro's coordinate system — [Panels Field View docs, via search snippet](https://ftcontrol.bylazar.com/docs/panels/fieldview/); also used as `PEDRO_PATHING` preset in [Mona Shores PR #70](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/pull/70)
- Panels 1.0.6 was built against FTC SDK 12.0.0, and the docs changelog references FullPanels 1.0.15. The Panels Docs plugin v1.0.9 is dated 7 Oct 2026 — [Panels docs](https://panels.bylazar.com/docs/com.bylazar.docs/)

### Inferences
- **FTC Dashboard has, Panels may lack:** a mature, simple, widely documented `TelemetryPacket` and Canvas API, and roughly 8 years of samples. Whether Panels has an equivalent hardware view, config editor or error view was not checked here; see the Panels researcher's notes.
- **Panels has, FTC Dashboard lacks today:** a plugin architecture, a built-in Pedro field-coordinate preset (with FTC Dashboard you must apply a Canvas translation and rotation yourself), and a released record/replay or loop-time view (those exist in FTC Dashboard only as unmerged PRs). Panels also already targets SDK 12.0.0.
- No 2026-27 season field image appears in FTC Dashboard's commits since kickoff (12 Sep 2026). Teams would need to use the custom image mechanism until a new release ships.

### Gaps
- I could not verify whether FTC Dashboard's 2026-27 field image is absent from the client bundle; I only checked that no commit mentions it.
- I found no authoritative, side-by-side feature matrix between FTC Dashboard and Panels.

## 3. Compatibility with the FTC SDK for 2026-27 (SDK 12.0) and modern Gradle/AGP

### Takeaway
FTC Dashboard 0.6.0 was last explicitly updated for SDK v11 (Dec 2025) and was released before SDK 12.0 (12 Sep 2026). No commit, release or maintainer statement confirms SDK 12 support, and no open issue reports SDK 12 breakage. It will most likely build, because Gradle resolves its SDK 10.0.0 dependencies up to the project's 12.0.0, but this is **unverified on hardware**.

### Cited Findings
- FTC SDK v12.0 was released 12 Sep 2026 as the official 2026/2027 release and requires Android Studio Narwhal 3 Feature Drop or later. Earlier releases: v11.2 on 15 Jul 2026, v11.2.1 on 31 Jul 2026, v11.0 on 6 Sep 2025 — [FtcRobotController releases](https://github.com/FIRST-Tech-Challenge/FtcRobotController/releases)
- The dashboard 0.6.0 POM declares compile dependencies on `org.firstinspires.ftc:{RobotCore,Hardware,RobotServer,FtcCommon}:10.0.0`, plus `com.acmerobotics.dashboard:core:0.6.0`, `androidx.annotation:1.1.0` and `org.nanohttpd:nanohttpd-websocket:2.3.1` — [dashboard-0.6.0.pom](https://acmerobotics.z13.web.core.windows.net/com/acmerobotics/dashboard/dashboard/0.6.0/dashboard-0.6.0.pom)
- "Update to SDK v11 (#212)" was committed on 24 Dec 2025. No commit through 7 Sep 2026 mentions SDK 12 — [commits](https://github.com/acmerobotics/ftc-dashboard/commits/master); no open PR mentions SDK 12 or Gradle/AGP — [open PRs](https://github.com/acmerobotics/ftc-dashboard/pulls)
- For OpenRC or non-standard SDK dependencies, the docs say to add `exclude group: 'org.firstinspires.ftc'` — [Getting Started](https://acmerobotics.github.io/ftc-dashboard/gettingstarted)
- A community fork of the Road Runner quickstart (PR #557, 28 Sep 2026; closed and not merged) updated to FTC 12.0.0, AGP 8.13.2 and Gradle 9.1.0 while leaving "Road Runner, Dashboard, and MeepMeep dependencies unchanged". No hardware test results were reported — [road-runner-quickstart PR #557](https://github.com/acmerobotics/road-runner-quickstart/pull/557)
- The SDK 12 breaking change found concerns AprilTag (single vs. cluster detections). It does not concern dashboards — [RR PR #557](https://github.com/acmerobotics/road-runner-quickstart/pull/557)
- Newest open issue #238, "System Opmodes are not displayed" (13 Oct 2026, by Froze-N-Milk): the dashboard does not correctly show the name or controls of non-default system OpModes. It links FtcRobotController issue #2249, which concerns the SDK's handling of System-flavor OpModes registered dynamically by libraries (Sloth, Pedro, NextFTC, Panels). The SDK maintainer (cmacfarl) called that behavior "working as designed" — [ftc-dashboard #238](https://github.com/acmerobotics/ftc-dashboard/issues/238); [FtcRobotController #2249](https://github.com/FIRST-Tech-Challenge/FtcRobotController/issues/2249)
- The 2026-27 Pedro Pathing Quickstart pins SDK `12.0.0` artifacts. It shows how an SDK 12 project is set up but does not use FTC Dashboard — [Pedro Quickstart build.dependencies.gradle](https://raw.githubusercontent.com/Pedro-Pathing/Quickstart/master/build.dependencies.gradle)

### Inferences
- Because dashboard 0.6.0 compiled against SDK 10/11 APIs it uses (OpModeManagerImpl.Notifications, @OpModeRegistrar, WebHandlerManager), it will probably run on SDK 12 unless those internals changed. None of the sources I found report such a change. Treat it as "likely works, test before relying on it".
- AGP/Gradle should not be an obstacle: the dashboard is a plain AAR, and SDK 12 projects are on AGP 8.13.x / Gradle 9.1 per PR #557.

### Gaps
- I found no first-hand report, positive or negative, of dashboard 0.6.0 running on a Control Hub with SDK 12.0.
- The FTC Discord was not searched, since it is not web-indexed.

## 4. Compatibility with Pedro Pathing 3, NextFTC v2 and Road Runner

### Takeaway
Pedro Pathing 3 has moved away from depending on *any* third-party dashboard. Its tuning is a robot-hosted AutoTune web page, and its 2026-27 Quickstart includes neither Panels nor FTC Dashboard. Pedro's docs no longer contain the "Choosing a Dashboard" page. That page (Pedro 2 era) said Panels was fully supported, while FTC Dashboard could show the robot and graph state but could not live-tune Pedro constants. NextFTC v2 has no dashboard integration in either direction. Road Runner remains FTC Dashboard's native pairing, but its quickstart has been idle since Oct 2025.

### Cited Findings
- Pedro 3 "What's new" covers AutoTune ("a robot-hosted webpage that allows for automatic tuning" of the drivetrain, localizer and algorithm config) and built-in logging. It does not mention Panels or FTC Dashboard — [Pedro 3 docs](https://pedropathing.com/docs/pathing/pedro3); [Tuning](https://pedropathing.com/docs/pathing/tuning)
- Pedro 3 debugging uses `debug()` maps printed via `telemetry.addLine`, and loggers are any `Consumer<FollowerLog>` passed to `.withLogger(...)`. Neither page names a dashboard — [Debugging](https://pedropathing.com/docs/pathing/guide/debugging); [Logging](https://pedropathing.com/docs/pathing/reference/logging)
- The Pedro Quickstart (master, last commit 8 Oct 2026) depends only on SDK 12.0.0 plus `com.pedropathing:revhub:3.0.1` and `com.pedropathing:tuning:1.0.1`, with no Panels and no FTC Dashboard. The repo's About text still reads "Quickstart for PedroPathing and Panels integration" — [build.dependencies.gradle](https://raw.githubusercontent.com/Pedro-Pathing/Quickstart/master/build.dependencies.gradle); [Quickstart repo](https://github.com/Pedro-Pathing/Quickstart); [commits API](https://api.github.com/repos/Pedro-Pathing/Quickstart/commits?per_page=12)
- `com.pedropathing:tuning:1.0.0` (the version Biobuzz uses) depends at runtime on `com.pedropathing:revhub:3.0.0`, `dev.frozenmilk.sinister:Sloth:0.3.0` and `org.nanohttpd:nanohttpd-websocket:2.3.1`. It runs its own embedded web server — [tuning-1.0.0.pom](https://repo1.maven.org/maven2/com/pedropathing/tuning/1.0.0/tuning-1.0.0.pom)
- `com.pedropathing:telemetry` has no dependencies. Its sources are a gamepad-driven selectable OpMode menu (`Folder`, `Selector`, `SelectableOpMode`, ...), not a dashboard adapter. Only 1.0.0 (27 Sep 2025) is on Maven Central, although a 1.0.1 version bump was committed 29 Nov 2025 — [telemetry-1.0.0.pom](https://repo1.maven.org/maven2/com/pedropathing/telemetry/1.0.0/telemetry-1.0.0.pom); [repo tree](https://api.github.com/repos/Pedro-Pathing/Telemetry/git/trees/HEAD?recursive=1); [metadata](https://repo1.maven.org/maven2/com/pedropathing/telemetry/maven-metadata.xml); [commits](https://api.github.com/repos/Pedro-Pathing/Telemetry/commits?per_page=10)
- Historical (Pedro 2 era), from a search-engine snippet of `pedropathing.com/docs/pathing/dashboard`, which returned **HTTP 404 when fetched on 14 Oct 2026**: Panels is "fully supported" by Pedro (192.168.43.1:8001), while FTC Dashboard can display the robot on the field and graph state but "doesn't support live tuning of Pedro's constants". The Quickstart's `Drawing` class (in Tuning.java) can be adapted for FTC Dashboard by swapping the Panels annotations for `@Config` and the Panels telemetry calls for their FTC Dashboard equivalents — [Pedro "Choosing a Dashboard" (now 404)](https://pedropathing.com/docs/pathing/dashboard)
- One team reports that the Pedro `Drawing` class "ships in `com.pedropathing:telemetry`" and "was never updated for Pedro 3". They switched to Panels' built-in `PEDRO_PATHING` field preset (Sep 2026) — [Mona Shores PR #70](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/pull/70). This is a community claim and conflicts with the telemetry repo tree above, which contains no Drawing class.
- NextFTC v2 (nextftc.dev, NextFTCSuite) has modules units, linalg, control, hardware and robot, where robot is "built on PedroPathing's Ivy scheduler". It mentions neither FTC Dashboard nor Panels, `@Config` nor `@Configurable` — [nextftc.dev](https://nextftc.dev/); [NextFTCSuite](https://github.com/NextFTC/NextFTCSuite)
- Road Runner docs: the quickstart ships "with preinstalled Road Runner, FTC Dashboard, and tuning utilities". The manual install lists `com.acmerobotics.dashboard:dashboard:0.5.1`, and the current RR versions are core/actions 1.0.1 and ftc 0.1.25 — [RR installation docs](https://rr.brott.dev/docs/v1-0/installation/); [RR quickstart TeamCode/build.gradle](https://raw.githubusercontent.com/acmerobotics/road-runner-quickstart/master/TeamCode/build.gradle)
- Sloth (Dairy) users, a group that includes Biobuzz since Sloth is on its classpath, should use Sloth-compatible forks: `com.acmerobotics.slothboard:dashboard:0.3.2+0.6.0` ("fully supports hot reloading for Configuration (`@Config`) and OpModes") and `com.bylazar.sloth:fullpanels:0.3.2+1.0.13`. Road Runner setups must `exclude group: "com.acmerobotics.dashboard"` in that case — [Sloth README](https://github.com/Dairy-Foundation/Sloth)

### Inferences
- For Biobuzz's stack, FTC Dashboard and Panels are both "bring your own integration". Pedro 3 and NextFTC v2 don't call either one, so the choice depends on telemetry, graphing and field-drawing ergonomics, not library support.
- Panels keeps one Pedro-specific edge, its built-in Pedro field-coordinate preset. With FTC Dashboard you would write a small Canvas drawing helper and apply the translation and rotation yourself, since Pedro's origin is a 0-144 in corner and the dashboard field is centered.
- FTC Dashboard's strongest pairing (Road Runner) is irrelevant to a Pedro team.
- If Biobuzz uses Sloth hot reload (Sloth Load), the vanilla dashboard artifact would not hot-reload `@Config` classes; the Slothboard fork would be required. Whether Biobuzz uses Sloth Load, or only has Sloth as Pedro tuning's runtime dependency, should be checked; the local gradle files show no Sloth Load plugin.

### Gaps
- I could not read the original Pedro "Choosing a Dashboard" page (now 404, and archive.org was blocked). Its quoted content comes from a search snippet.
- I could not confirm whether the Pedro 3 Quickstart still contains a `Drawing` class, or what it targets.
- No NextFTC v2 docs page discusses dashboards.

## 5. Running FTC Dashboard and Panels together; migrating from @Configurable to @Config

### Takeaway
By design they use different ports. FTC Dashboard is served at :8080/dash via the SDK web server plus its own websocket server on :8000, and Panels uses :8001. The annotations are separate, and one class can carry both. At least one 2026-27 team saw a boot-time `BindException` crash loop with both installed alongside Pedro 3, while another kit runs both successfully on SDK 11.2.1 using the Sloth variants. Migration is mechanical and, for Biobuzz, small (3 files).

### Cited Findings
- FTC Dashboard starts `new NanoWSD(8000)` for its WebSocket/HTTP server and registers static files with the SDK web server under `/dash`. It also registers an "Enable/Disable Dashboard" OpMode in a "Dashboard" group via `@OpModeRegistrar`, and auto-enable defaults to true — [FtcDashboard.java](https://raw.githubusercontent.com/acmerobotics/ftc-dashboard/master/FtcDashboard/src/main/java/com/acmerobotics/dashboard/FtcDashboard.java)
- The URL is `192.168.43.1:8080/dash` (Control Hub) or `192.168.49.1:8080/dash` (phone RC) — [Getting Started](https://acmerobotics.github.io/ftc-dashboard/gettingstarted)
- The BrainStem 2026 starter kit runs both on SDK 11.2.1 with Sloth: Panels at `:8001`, Dashboard at `:8080/dash`. Tunables carry both `@Configurable` and `@Config` on the same class ("they are separate systems"), and one `MultipleTelemetry` `update()` "reaches Panels, Dashboard, and the Driver Station". It warns: "Do not add the vanilla artifacts alongside these — they clash at package level" (meaning the Sloth variants vs. vanilla artifacts) — [BrainStem FTC-Starter-Kit-2026](https://github.com/BrainStem-FIRST/FTC-Starter-Kit-2026)
- Mona-Shores-FTC-Robotics/biobuzz (a repo whose stack resembles Biobuzz's: Pedro 3, Panels, Limelight, Sloth Load) reported in Sep 2026 that "with Panels also installed the Robot Controller crash-loops on `BindException` and never boots". The exception was `java.net.BindException` in `NanoHTTPD$ServerRunnable` about 3.5 s into boot. It reproduced "with no team Java in the APK" and survived a cold boot, and removing either dashboard fixed it. The team dropped FTC Dashboard. Port, versions and root cause were not stated — [issue #69](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/issues/69); [PR #70](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/pull/70)
- No open FTC Dashboard issue mentions Panels or port conflicts — [issues](https://github.com/acmerobotics/ftc-dashboard/issues)
- Pedro tuning (AutoTune) also embeds `nanohttpd-websocket` — [tuning-1.0.0.pom](https://repo1.maven.org/maven2/com/pedropathing/tuning/1.0.0/tuning-1.0.0.pom)
- `@Config` scanning goes through the app's DexFile. It skips classes annotated `@Disabled` and certain ignored packages — [FtcDashboard.java](https://raw.githubusercontent.com/acmerobotics/ftc-dashboard/master/FtcDashboard/src/main/java/com/acmerobotics/dashboard/FtcDashboard.java)
- `@Config` rules: the class is annotated, and fields are `static`, non-`final` (public, per the sample usage). For Kotlin, use `object` plus `@JvmField var`. Code-side changes are not pushed to the browser until a refresh — [Features docs](https://acmerobotics.github.io/ftc-dashboard/features). Panels behaves the same way: it pushes values only on browser connect, a browser edit, or `PanelsConfigurables.refreshClass` — [Mona Shores issue #69](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/issues/69)

### Inferences
- Migration for Biobuzz: in `Intake.java`, `Intermediate.java` and `Poses.java`, replace the `com.bylazar...Configurable` import and annotation with `com.acmerobotics.dashboard.config.Config`. Swap any Panels telemetry or field calls for `TelemetryPacket` / `MultipleTelemetry` / `fieldOverlay()`. Then remove `com.bylazar:fullpanels` and the `mymaven.bylazar.com` repo, and add `maven.brott.dev` plus `dashboard:0.6.0`, or Slothboard if Sloth Load is used. Static fields that are already `public static` and non-final need no change.
- A transition period with both installed is possible in principle, by annotating both. Given the Mona Shores report, though, the team should boot-test the exact combination (FTC Dashboard + fullpanels + Pedro tuning AutoTune + Sloth, all with embedded NanoHTTPD servers) before relying on it. The third server (AutoTune) is a plausible but unverified factor in that BindException.

### Gaps
- I could not confirm the websocket port Panels uses, beyond its HTTP :8001, or the port Pedro AutoTune listens on. So the root cause of the reported BindException is unknown.
- I could not confirm whether `@Config` and `@Configurable` both handle nested objects, enums, and the same field types identically.

## 6. Known bugs, loop-time overhead, connectivity issues

### Takeaway
Telemetry transmission is asynchronous: packets are queued and a background thread sends them every 100 ms by default, dropping packets past 100 queued. Per-loop cost should be small, but I found no published loop-time benchmarks. The known open issues are mostly long-standing UX and robustness items. The newest are an "offline" state with a stray "Hardware" opMode, an init race condition, and system OpMode display problems.

### Cited Findings
- `DEFAULT_TELEMETRY_TRANSMISSION_INTERVAL = 100` (ms), adjustable via `setTelemetryTransmissionInterval`. `sendTelemetryPacket` appends to a synchronized queue and returns at once, and it drops new packets when more than 100 are pending ("TODO: circular buffer" in the code). A single "dash telemetry" thread batches the queue, keeps only the latest non-empty field overlay, sends, then sleeps — [DashboardCore.java](https://raw.githubusercontent.com/acmerobotics/ftc-dashboard/master/DashboardCore/src/main/java/com/acmerobotics/dashboard/DashboardCore.java)
- `DEFAULT_IMAGE_QUALITY = 50` for the camera stream and `GAMEPAD_WATCHDOG_INTERVAL = 500` ms — [FtcDashboard.java](https://raw.githubusercontent.com/acmerobotics/ftc-dashboard/master/FtcDashboard/src/main/java/com/acmerobotics/dashboard/FtcDashboard.java)
- Open issues with connectivity or robustness relevance (23 open in total):
  - #238 System OpModes not displayed (13 Oct 2026)
  - #210 "Dashboard 'offline' and odd 'Hardware' opMode has appeared" (8 Dec 2025)
  - #209 "instance initialisation race condition" (28 Nov 2025)
  - #131 gamepad support broken on browsers (Feb 2023)
  - #57 OpMode start/stop button delay (Mar 2021)
  - #103 `TelemetryPacket.addData()` returns null (Nov 2022)

  Source: [issues](https://github.com/acmerobotics/ftc-dashboard/issues)
- Pending fixes: #236 enum config values set to null or breaking the Config view (29 Sep 2026), and #235 Error View and Logging fixes — [open PRs](https://github.com/acmerobotics/ftc-dashboard/pulls)
- Competition and bandwidth: "The dashboard cannot be used during matches pursuant to RS09 (2021-2022 Game Manual Part 1)". The docs recommend the "Disable Dashboard" menu item or OpMode during gameplay and ask teams to "limit bandwidth usage, especially high-framerate camera streams" in pits — [Competition docs](https://acmerobotics.github.io/ftc-dashboard/competition) (rule citation is historical, 2021-22)

### Inferences
- The async send design means `sendTelemetryPacket` itself should add little loop time. Building large packets or Canvas overlays every loop, plus JSON serialization on the send thread, is the more likely CPU cost. A Loop Time view (#233) is still only a PR.
- Packet dropping beyond 100 queued packets means high-rate graphing can silently lose samples when the client is slow.

### Gaps
- I found no measured loop-time overhead (ms) for FTC Dashboard or Panels on a Control Hub, and no quantitative comparison.
- I did not verify the current 2026-27 Competition Manual's rule number or wording on third-party dashboards during matches.
- Reddit, the FTC forums and the FTC Discord returned no indexed head-to-head discussions of the two dashboards.

## 7. License

### Takeaway
FTC Dashboard is MIT-licensed (ACME Robotics). The bundled FtcRobotController portion of the repo is under FIRST's BSD-style license.

### Cited Findings
- The LICENSE file contains the MIT License for FtcDashboard and TeamCode (copyright ACME Robotics, 2018-2022) and a FIRST BSD-style license for FtcRobotController (2014-2022) — [LICENSE](https://github.com/acmerobotics/ftc-dashboard/blob/master/LICENSE)
- The Panels repo links a LICENSE.md, but the license type was not shown in what I fetched — [ftcontrol-panels](https://github.com/ftcontrol/ftcontrol-panels)

### Inferences
- MIT poses no obstacle to forking or vendoring, which is relevant given the PR backlog: a team could build an unreleased snapshot via `publishToMavenLocal`, per [Getting Started (Advanced)](https://acmerobotics.github.io/ftc-dashboard/gettingstarted).

### Gaps
- I did not determine Panels' license for a license comparison.
