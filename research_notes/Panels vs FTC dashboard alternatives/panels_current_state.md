# Panels (FTControl Panels, `com.bylazar:fullpanels`): current state as of October 2026

Research date: 2026-10-14. Primary sources were the bylazar Maven metadata, the `ftcontrol/ftcontrol-panels` GitHub repo (commits, issues, changelogs kept in each plugin's `web/config.ts`), the Pedro Pathing and NextFTC repos, POMs on Maven Central, and the team's own Gradle files. Web search found almost no Reddit or forum discussion, so the community view comes mostly from GitHub issues. Changelog dates in the Panels source use D.M.YYYY format, and they are converted to ISO dates below.

## Q1. Latest version, how far behind 1.0.13 is, and what changed

### Takeaway
The latest release is `com.bylazar:fullpanels:1.0.18` (2026-10-07). That puts 1.0.13 (2026-09-13) five releases behind. Every release since 1.0.13 came out between 2026-10-02 and 2026-10-07, and those releases fix two serious bugs that hit this team's exact setup: the missing BIOBUZZ field, and `@Configurable` values that silently do nothing when Sloth is on the classpath. Upgrading also brings new requirements: FTC SDK 12.0.0, the Dairy Maven repo, and Kotlin Gradle plugin 2.4.10 or newer for Kotlin projects.

### Cited Findings
- The Maven metadata lists 19 versions (1.0.0 through 1.0.18). Both `latest` and `release` are `1.0.18`, and `lastUpdated` is `20261007070335`. — [bylazar Maven metadata](https://mymaven.bylazar.com/releases/com/bylazar/fullpanels/maven-metadata.xml)
- The panels.bylazar.com homepage shows "v1.0.18". — [Panels homepage](https://panels.bylazar.com/)
- FullPanels changelog from 1.0.13 onward:
  - **1.0.13 (2026-09-13).** Field updated to 1.0.7 "with BIOBUZZ field images". — [FullPanels config.ts changelog](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/FullPanels/web/config.ts)
  - **1.0.14 (2026-10-02).** "Restored packaged web assets and updated Field to 1.0.8 to restore the BIOBUZZ Field widget". — [FullPanels changelog](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/FullPanels/web/config.ts)
  - **1.0.15 (2026-10-03).** "Updated Panels and all included plugins for FTC SDK 12.0.0 with pinned frontend core 1.1.44". Upgrade note: "Update the FTC SDK to 12.0.0". — [FullPanels changelog](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/FullPanels/web/config.ts)
  - **1.0.16 (2026-10-03).** "Integrated Sloth support and Dairy build improvements contributed by Oscar Chevalier (@Froze-N-Milk) via PR #15". Upgrade note: add `maven { url = uri("https://repo.dairy.foundation/releases") }`, and "FTC SDK 12.0.0 is required." — [FullPanels changelog](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/FullPanels/web/config.ts)
  - **1.0.17 (2026-10-03).** Docs 1.0.8 with corrected Prerequisites (the Dairy repository for Sloth, and "No compileSdk/minSdk changes are needed"). — [FullPanels changelog](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/FullPanels/web/config.ts)
  - **1.0.18 (2026-10-07).** Docs 1.0.9, "which now requires kotlin-gradle-plugin 2.4.10 or newer for Kotlin consumers". — [FullPanels changelog](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/FullPanels/web/config.ts)
- Configurables plugin 1.0.7 (2026-10-03) added "Sloth-aware configurable scanning and unloading, with class-based refresh APIs". Upgrade note: "Use refreshClass(Class) in Java or refreshClass(KClass) in Kotlin; add the Dairy releases repository to resolve Sloth dependencies." — [Configurables config.ts changelog](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/Configurables/web/config.ts)
- Current plugin versions inside FullPanels 1.0.18, all with 2026-10-03 entries: Telemetry 1.0.7, Field 1.0.10, Graph 1.0.6, Capture 1.0.5, CameraStream 1.0.2, Configurables 1.0.7. The 2026-10-03 entries say "Built against FTC SDK 12.0.0" and "Migrated to Dairy build tooling". — [Telemetry](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/Telemetry/web/config.ts), [Field](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/Field/web/config.ts), [Graph](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/Graph/web/config.ts), [Capture](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/Capture/web/config.ts), [CameraStream](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/CameraStream/web/config.ts)
- FullPanels now pulls in Sloth (`dev.frozenmilk.sinister:Sloth:0.3.2`), so consumers need `https://repo.dairy.foundation/releases` among their repositories. — [AGENTS.md (repo release guide)](https://github.com/ftcontrol/ftcontrol-panels/blob/main/AGENTS.md)
- The Prerequisites doc gives the "Before updating" steps: back up the code, update the FTC SDK to 12.0.0, make sure both the Bylazar and Dairy repos are present, run a clean build or delete the build folders "so old Panels assets are not reused", check each plugin's changelog, and switch from the `com.bylazar.sloth:*` artifacts back to the official `com.bylazar:*` ones, "which now support Sloth directly". — [Prerequisites.svelte (docs source)](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/Docs/web/src/docs/Prerequisites.svelte)
- The Prerequisites doc also says: "Panels is built with Kotlin 2.4.10. Kotlin consumers must use Kotlin 2.4.10 or newer (kotlin-gradle-plugin >= 2.4.10), or the compiler will reject Panels classes with an incompatible metadata version." — [Prerequisites.svelte](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/Docs/web/src/docs/Prerequisites.svelte)
- The repo publishes no GitHub Releases and no git tags. Release notes exist only in each plugin's `web/config.ts` changelog, which the docs site renders. — [GitHub repo](https://github.com/ftcontrol/ftcontrol-panels) (the Releases and Tags API calls returned empty lists on 2026-10-14)
- Historical release dates for 1.0.0 to 1.0.12: 1.0.0 to 1.0.2 on 2025-08-26, 1.0.3 on 2025-08-27, 1.0.4 on 2025-08-31, 1.0.5 on 2025-09-04, 1.0.6 on 2025-09-09 (SDK 11.0.0 and DECODE field), 1.0.7 on 2025-10-08 (Graph rewrite), 1.0.8 on 2025-10-23 (Telemetry 1.0.4: `addLine`, `setMsTransmissionInterval`, `%%` formatter fix), 1.0.9 on 2025-11-01 (Camera Stream plugin added), 1.0.10 on 2025-11-14, 1.0.11 on 2025-12-06, and 1.0.12 on 2025-12-12 ("Reduced Panels logs amount"). — [FullPanels changelog](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/FullPanels/web/config.ts)

### Inferences
- 1.0.13 is a poor version to stay on. It was the first release with the 2026-27 BIOBUZZ field images, but issue #18 says it shipped without a working Field widget. It also predates Sloth support. Both defects are fixed in 1.0.14 and 1.0.16.
- The team's Kotlin Gradle plugin is 2.2.10 (local `build.gradle`) and TeamCode applies `org.jetbrains.kotlin.android`. By the docs, that makes the team a "Kotlin consumer", so moving to 1.0.18 likely also means raising the Kotlin Gradle plugin to 2.4.10 or newer. Version 2.4.10 exists on Maven Central, and 2.4.21 is the newest stable release there ([KGP metadata](https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin-gradle-plugin/maven-metadata.xml)). The team already has FTC SDK 12.0.0 and the Dairy repo in `build.dependencies.gradle`.
- One possible API break is the Configurables "class-based refresh API" (`refreshClass(Class)`). It only matters if team code calls the refresh API directly. The local grep found 6 `@Configurable` references in TeamCode, and none were checked for refresh calls.

### Gaps
- No changelog or other source says whether 1.0.15 to 1.0.17 were also compiled with Kotlin 2.4.x, which would make them break on Kotlin 2.2.10. Only 1.0.18 states the requirement explicitly.
- I found no statement on whether the `refreshClass` change removed or renamed an older API, so it is unclear whether existing calls would break.

## Q2. Release cadence, maintenance activity, maintainers, bus factor

### Takeaway
Panels is in active development as of October 2026, but by essentially one maintainer, and the pace is uneven. FullPanels shipped no release between 2025-12-12 and 2026-09-13, which covers the whole 2025-26 competition season. Releases then came in a burst in September and October 2026. The bus factor is about 1.

### Cited Findings
- Repo stats on 2026-10-14: 40 stars, 10 forks, 4 open issues. Created 2025-07-08, last pushed 2026-10-07, not archived. — [ftcontrol/ftcontrol-panels](https://github.com/ftcontrol/ftcontrol-panels)
- Contributors API: MonsieurLazar (Lazar) has 429 commits and Froze-N-Milk (Oscar Chevalier, Dairy Foundation) has 21. No other contributors are listed. — [Contributors](https://github.com/ftcontrol/ftcontrol-panels/graphs/contributors)
- The author's portfolio lists the project as "Type: Solo Project", with a timeline of "Jun 11, 2025 - Present". — [bylazar.com FTControl Panels V1](http://bylazar.com/projects/ftcontrol-panels-v1/)
- Commits on `main` are dense from August to December 2025 (for example many commits on 2025-08-24 to 2025-08-27, and 2025-10-08). Lazar's own commits after 2025-12-12 are dated 2026-01-09 ("updated ftc-panels dependency"), 2026-09-13 ("feat: new field image"), and 2026-10-02 to 2026-10-07 (release work). Commits dated January, February and September 2026 by Oscar Chevalier and Froze-N-Milk arrived on `main` through the PR #15 merge on 2026-10-02 to 2026-10-03. — [Commit history](https://github.com/ftcontrol/ftcontrol-panels/commits/main)
- PR #15 ("Sloth & Other improvements", Froze-N-Milk) was opened 2025-12-26 and closed or merged 2026-10-03, about 9 months later. — [PR #15](https://github.com/ftcontrol/ftcontrol-panels/pull/15)
- In October 2025 the maintainer wrote, "A version that works with sloth is coming soon". Sloth support shipped in FullPanels 1.0.16 on 2026-10-03. — [Issue #7](https://github.com/ftcontrol/ftcontrol-panels/issues/7); [FullPanels changelog](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/FullPanels/web/config.ts)
- Issue response examples:
  - #18 (2026-09-22): first maintainer reply 2026-10-02, about 10 days later.
  - #19 (2026-09-22): closed by the maintainer 2026-10-03. The community diagnosed the cause in the meantime.
  - #7 (2025-10-24): maintainer replied 2025-10-26. A user added on 2026-07-09: "It's 2026 - still experiencing the same issue, specifically on gamepad. Using v1.0.12."
  - #17 (2026-03-04, Field `cursorHeading` not implemented): still open with 0 comments.
  - #16 (2026-01-01, Combined Gamepad suggestion): still open.
  - #1 (2025-08-31, non-static configurables): still open.
  - #13 (2025-11-28, improved telemetry displays): still open.
  - Sources: [#18](https://github.com/ftcontrol/ftcontrol-panels/issues/18), [#19](https://github.com/ftcontrol/ftcontrol-panels/issues/19), [#7](https://github.com/ftcontrol/ftcontrol-panels/issues/7), [#17](https://github.com/ftcontrol/ftcontrol-panels/issues/17), [#16](https://github.com/ftcontrol/ftcontrol-panels/issues/16), [#1](https://github.com/ftcontrol/ftcontrol-panels/issues/1), [#13](https://github.com/ftcontrol/ftcontrol-panels/issues/13)
- A community fork, `Dairy-Foundation/ftcontrol-panels`, was last pushed 2026-09-17 and published the separate `com.bylazar.sloth:fullpanels` artifacts (latest `0.3.2+1.0.13`, 2026-09-17). Official docs now tell users to switch back to `com.bylazar:*`. — [Dairy fork](https://github.com/Dairy-Foundation/ftcontrol-panels); [Dairy Maven metadata](https://repo.dairy.foundation/releases/com/bylazar/sloth/fullpanels/maven-metadata.xml); [Prerequisites.svelte](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/Docs/web/src/docs/Prerequisites.svelte)
- On 2026-10-02 the repo added an `AGENTS.md` "repository and release workflow guide". It describes publishing to a sibling `ftcontrol-maven` repo and updating both quickstarts for each release. — [AGENTS.md](https://github.com/ftcontrol/ftcontrol-panels/blob/main/AGENTS.md)

### Inferences
- The project is maintained, but the pattern is long quiet stretches followed by pre-season bursts. Fixes for in-season breakage, such as the Sloth incompatibility reported in October 2025, took about a year to ship. A team that hits a Panels bug mid-season should not count on a quick upstream fix.
- Bus factor is about 1 (Lazar). Dairy Foundation (Oscar Chevalier) is the only other contributor and has already forked the project once. That fork offers some fallback if Lazar stops maintaining it.
- `AGENTS.md` and the recent Conventional-Commit-style messages suggest the October 2026 releases were done with an AI coding agent's help. This is unverified and does not by itself indicate quality either way.

### Gaps
- I could not check Discord activity or response times. Most FTC library support happens on Discord and none of it is public.
- The closed PRs #5 (`addLine`) and #8 (Android logging) were closed in June 2026. I did not confirm whether they were merged or rejected.

## Q3. Feature set, and what the `fullpanels` bundle includes

### Takeaway
Panels is a browser dashboard served from the robot and built from plugins. `fullpanels` bundles the core plus 15 official plugins: OpMode control, telemetry, configurables, field drawing, graphing, gamepad, capture/replay, Limelight proxy, camera stream, lights, battery, pinger, themes, docs and utils. Custom plugins use a Svelte frontend with a Kotlin backend.

### Cited Findings
- `includedPluginsIDs` in FullPanels 1.0.18: `com.bylazar.panels`, `battery`, `capture`, `configurables`, `docs`, `field`, `gamepad`, `limelightproxy`, `opmodecontrol`, `telemetry`, `themes`, `utils`, `pinger`, `graph`, `lights`, `camerastream`. — [FullPanels config.ts](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/FullPanels/web/config.ts)
- The FullPanels Gradle module brings in each plugin through `api(project(...))`. FTC SDK modules are `compileOnly`. — [FullPanels build.gradle.kts](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/FullPanels/build.gradle.kts)
- Homepage features: Telemetry (mirrors Driver Hub telemetry), Field (drawable canvas with pathing-library support), Graph (live data over time, aimed at PID tuning), Lights (goBILDA-style RGB indicators), Capture (record and replay debug data), Configurables (real-time tunable variables), integrated offline docs, Battery and Pinger, OpMode Control (Driver Station-style, with timers), Camera Stream ("optimized FTC webcam streaming"), Limelight 3A integration, support for up to 2 gamepads, Themes (resizable grid layouts, tabbed widget groups), and per-plugin update checks. — [Panels homepage](https://panels.bylazar.com/)
- README: OpMode control, real-time telemetry, field view, graph view, capture mode ("Record and replay matches"), configurables ("Tune your robot in real time without uploading code again"), Limelight support ("without USB connection"), and plugins. It is written in Kotlin and also works from Java. — [README](https://github.com/ftcontrol/ftcontrol-panels)
- The default FullPanels layout places OpMode Control, Configurables plus ChangedConfigurables, Combined Gamepad plus Capture, Field, and Telemetry widgets, with Battery and Ping in the navbar. — [FullPanels config.ts templates](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/FullPanels/web/config.ts)
- Plugins can be installed individually (`com.bylazar:panels` core plus any of `opmodecontrol`, `telemetry`, `configurables`, `field`, `gamepad`, `graph`, `capture`, `limelightproxy`, `camerastream`, `themes`, `lights`, `battery`, `pinger`, `utils`). The docs warn: "Do not mix fullpanels with the individual plugins". — [Prerequisites.svelte](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/Docs/web/src/docs/Prerequisites.svelte)
- Configurables 1.0.5 (2025-12-06) added a "sorter annotation". Configurables only work on static fields. Issue #1 asks for non-static support and has been open since 2025-08-31. — [Configurables changelog](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/Configurables/web/config.ts); [Issue #1](https://github.com/ftcontrol/ftcontrol-panels/issues/1)
- Field 1.0.5 (2025-11-14) added "Custom Presets can be shown in widget". Field 1.0.6 (2025-12-06) fixed "field and OS zoom level". Field images now include the BIOBUZZ light and dark presets (Field 1.0.7 and later). — [Field changelog](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/Field/web/config.ts)

### Inferences
- Only the static-field `@Configurable` workflow is covered. Teams that want to tune instance fields, such as subsystem objects created per OpMode, are not served (issue #1).

### Gaps
- I did not find any Panels feature for persistent on-robot logging to file, comparable to AdvantageScope/WPILog-style logs. "Capture" is record-and-replay inside the dashboard, and I could not confirm whether captures persist on disk or how large they can get.

## Q4. Integration with Pedro Pathing 3 and NextFTC v2

### Takeaway
Pedro Pathing 3 has moved away from Panels. Its quickstart no longer includes Panels, its docs never mention Panels, and its tuning module (`com.pedropathing:tuning`, AutoTune) pulls in Sloth. Sloth silently broke Panels `@Configurable` in 1.0.13 and earlier. NextFTC v2 also pulls in Sloth at runtime and has no Panels integration or docs. Official Panels added Sloth support in 1.0.16, which is the version that makes this stack workable.

### Cited Findings
- **Pedro 3 quickstart today:** `build.dependencies.gradle` contains only the FTC SDK 12.0.0 artifacts, `com.pedropathing:revhub:3.0.1` and `com.pedropathing:tuning:1.0.1`. There is no Panels dependency and no bylazar repo. — [Pedro Quickstart build.dependencies.gradle (master)](https://github.com/Pedro-Pathing/Quickstart/blob/master/build.dependencies.gradle)
- **Historical, Pedro 2.x quickstart (2026-05-08 commit `4d160daeed`):** included `com.pedropathing:ftc:2.1.2`, `com.pedropathing:telemetry:1.0.0`, `com.bylazar:fullpanels:1.0.12` and the `mymaven.bylazar.com` repo. The Pedro 3.0.0 commit (`f96afa63c8`, 2026-09-10) replaced these with `revhub:3.0.0` and `tuning:1.0.0` and dropped Panels. — [Quickstart @4d160daeed](https://github.com/Pedro-Pathing/Quickstart/blob/4d160daeed/build.dependencies.gradle); [Quickstart @f96afa63c8](https://github.com/Pedro-Pathing/Quickstart/blob/f96afa63c8/build.dependencies.gradle); [file history](https://github.com/Pedro-Pathing/Quickstart/commits/master/build.dependencies.gradle)
- Pedro 3 docs describe AutoTune as "a robot-hosted webpage that allows for automatic tuning" of the drivetrain, localizer and Foresight. Pedro 3 logging is through `Follower.withLogger(Consumer<FollowerLog>)`. Searching all 56 `.mdx` pages in the Pedro Docs repo found no mention of "panels" or "bylazar". — [Pedro Docs: pedro3.mdx](https://github.com/Pedro-Pathing/Docs/blob/master/content/docs/pathing/pedro3.mdx); [tuning/index.mdx](https://github.com/Pedro-Pathing/Docs/blob/master/content/docs/pathing/tuning/index.mdx); [reference/logging.mdx](https://github.com/Pedro-Pathing/Docs/blob/master/content/docs/pathing/reference/logging.mdx)
- **Historical:** a search snippet for Pedro's "Choosing a Dashboard" page said users "may use either Panels or the FTC Dashboard". The URL returns HTTP 404 as of 2026-10-14. — [pedropathing.com/docs/pathing/dashboard (404)](https://pedropathing.com/docs/pathing/dashboard)
- `com.pedropathing:telemetry` (1.0.0, last updated 2025-09-27) is a selectable-OpMode menu library (`SelectableOpMode`, `Selector`, `Folder`). It depends only on `compileOnly` FTC SDK artifacts and does **not** use Panels. — [Pedro-Pathing/Telemetry](https://github.com/Pedro-Pathing/Telemetry); [Maven Central metadata](https://repo1.maven.org/maven2/com/pedropathing/telemetry/maven-metadata.xml)
- The `com.pedropathing:tuning` POMs declare runtime dependencies on `dev.frozenmilk.sinister:Sloth:0.3.0` (tuning 1.0.0) and `Sloth:0.3.2` (tuning 1.0.1), plus nanohttpd-websocket. The newest tuning versions are 1.0.2 and 1.0.3-beta.1 (metadata updated 2026-10-09). — [tuning-1.0.0.pom](https://repo1.maven.org/maven2/com/pedropathing/tuning/1.0.0/tuning-1.0.0.pom); [tuning-1.0.1.pom](https://repo1.maven.org/maven2/com/pedropathing/tuning/1.0.1/tuning-1.0.1.pom); [tuning metadata](https://repo1.maven.org/maven2/com/pedropathing/tuning/maven-metadata.xml)
- **The conflict (issue #19, 2026-09-22 to 2026-10-03):** "Setting the value of @Configurable variables does not seem to be working". It worked on SDK 11.2.1 but not 12.0. Several teams on SDK 12 and the latest Panels confirmed it. Root cause reported by a user: "com.pedropathing:tuning bundles Sloth, and Sloth silently breaks Panels Configurables by making a second copy of all classes. Panels is updating a different set of static variables than the OpCodes are using." The interim workaround was to comment out `com.pedropathing:tuning` except while tuning. The maintainer closed it on 2026-10-03: "Panels works with sloth now thanks to Oscar's PR". — [Issue #19](https://github.com/ftcontrol/ftcontrol-panels/issues/19)
- Pedro issue #188 ("Conflict between Panels and PedroPathing Tuning", 2026-09-29): a Pedro maintainer replied, "You need to use Sloth Panels instead of FTControl Panels". A user asked that the issue be documented because "panels config not working is silent." — [PedroPathing #188](https://github.com/Pedro-Pathing/PedroPathing/issues/188)
- The Sloth README says Sloth "includes a drop-in replacement of FTC Dashboard and Panels" and documented `com.bylazar.sloth:fullpanels:0.3.2+1.0.13`. That Sloth-specific route is now superseded by official 1.0.16 and later. — [Sloth README](https://github.com/Dairy-Foundation/Sloth); [Prerequisites.svelte](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/Docs/web/src/docs/Prerequisites.svelte)
- **NextFTC v2:** the `dev.nextftc.v2:robot:0.2.1` POM declares a runtime dependency on `dev.frozenmilk.sinister:Sloth:0.3.2`, plus `com.pedropathing.ivy:core:1.1.1` and kotlin-stdlib 2.3.0. The README describes "reflection-based OpMode registration, built on PedroPathing's Ivy scheduler". — [robot-0.2.1.pom](https://repo1.maven.org/maven2/dev/nextftc/v2/robot/0.2.1/robot-0.2.1.pom); [NextFTCSuite README](https://github.com/NextFTC/NextFTCSuite); [robot/build.gradle.kts](https://github.com/NextFTC/NextFTCSuite/blob/main/robot/build.gradle.kts)
- None of the 34 Markdown pages in the NextFTC-Docs repo mention "panels" or "bylazar". NextFTCSuite's version catalog has no Panels dependency. — [NextFTC-Docs](https://github.com/NextFTC/NextFTC-Docs); [NextFTCSuite libs.versions.toml](https://github.com/NextFTC/NextFTCSuite/blob/main/gradle/libs.versions.toml)
- **The team's setup (local files):** `com.bylazar:fullpanels:1.0.13`, `com.pedropathing:tuning:1.0.0`, `com.pedropathing:telemetry:1.0.0`, `com.pedropathing:revhub:3.0.0`, `com.pedropathing.ivy:pedro:1.1.1`, `dev.nextftc.v2:robot:0.2.1`, an explicit `dev.frozenmilk.sinister:Sloth:0.3.2`, FTC SDK 12.0.0, the Dairy repo, AGP 9.3.3 and Kotlin Gradle plugin 2.2.10. — local `build.dependencies.gradle`, `build.gradle`, `TeamCode/build.gradle`

### Inferences
- The team's current mix matches the issue #19 failure mode: Panels 1.0.13 plus Sloth from Pedro tuning, NextFTC v2 and an explicit dependency. Their `@Configurable` edits may be silently ignored at runtime. Upgrading to fullpanels 1.0.16 or later is the documented fix. The team should confirm on-robot that changing a configurable actually changes behavior.
- NextFTC v2 brings in Sloth as well, so removing Pedro's tuning module would likely not avoid the conflict on 1.0.13. This is unverified: no issue reports NextFTC v2 together with Panels.
- Panels is no longer a first-class part of the Pedro ecosystem. Pedro 3 tuning and visualization now go through Pedro's own AutoTune web page, so Panels' Field widget is optional for Pedro 3 teams. Any drawing of Pedro paths in Panels would have to be wired up by the team.

### Gaps
- I found no official example combining Panels with Pedro 3 or NextFTC v2.
- I could not confirm whether Pedro 3 offers a Panels drawing helper, as Pedro 2.x quickstarts reportedly did with `Drawing.java`. No current docs mention one.

## Q5. Compatibility with FTC SDK 12.0.0 (2026-27) and Android Studio, Gradle and AGP

### Takeaway
FullPanels 1.0.15 and later target FTC SDK 12.0.0, the current SDK on Maven Central. FullPanels 1.0.16 and later require the Dairy repo, and 1.0.18 requires Kotlin Gradle plugin 2.4.10 or newer for Kotlin projects. The official quickstarts build on AGP 8.7.0. Nothing documents support for AGP 9.x, which the team uses.

### Cited Findings
- FTC `RobotCore` on Maven Central has `latest` and `release` 12.0.0, with `lastUpdated` 2026-09-12. — [RobotCore metadata](https://repo1.maven.org/maven2/org/firstinspires/ftc/RobotCore/maven-metadata.xml)
- FullPanels 1.0.15 (2026-10-03) was "Updated ... for FTC SDK 12.0.0". 1.0.16 states "FTC SDK 12.0.0 is required." — [FullPanels changelog](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/FullPanels/web/config.ts)
- Issue #19 reports configurables worked on SDK 11.2.1 but not on 12.0 with Pedro 3 tuning, as covered under Q4. — [Issue #19](https://github.com/ftcontrol/ftcontrol-panels/issues/19)
- The Panels Java and Kotlin quickstarts (both pushed 2026-10-07) use FTC SDK 12.0.0 artifacts, `com.bylazar:fullpanels:1.0.18`, both the bylazar and Dairy repos, and `com.android.tools.build:gradle:8.7.0`. The Kotlin quickstart also uses `kotlin-gradle-plugin:2.4.10`. — [Panels-Quickstart-Java](https://github.com/ftcontrol/Panels-Quickstart-Java); [Panels-Quickstart-Kotlin](https://github.com/ftcontrol/Panels-Quickstart-Kotlin)
- For comparison, the Pedro quickstart uses AGP plugin `8.13.2`. — [Pedro Quickstart build.gradle](https://github.com/Pedro-Pathing/Quickstart/blob/master/build.gradle)
- The compileSdk requirement is gone. Issue #4 ("Able to avoid `compileSdkVersion 34` requirement?") was closed 2026-10-03, and the 1.0.17 notes say "No compileSdk/minSdk changes are needed." — [Issue #4](https://github.com/ftcontrol/ftcontrol-panels/issues/4); [FullPanels changelog](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/FullPanels/web/config.ts)
- Building Panels itself needs JDK 25 and Gradle 9.1.0 ("Android Studio's bundled JDK 21 cannot load the current Dairy Gradle plugins"). This applies to Panels contributors, not to teams consuming the artifacts. — [AGENTS.md](https://github.com/ftcontrol/ftcontrol-panels/blob/main/AGENTS.md)

### Inferences
- For this team, upgrading means bumping `fullpanels` to 1.0.18 and the Kotlin Gradle plugin from 2.2.10 to 2.4.10 or newer, then doing a clean build. The team already has the Dairy repo and SDK 12.0.0. With AGP 9.3.3 the team is ahead of every documented quickstart, so expect to verify the build locally.

### Gaps
- Panels docs say nothing about AGP 9.x compatibility, and I found no reports either way. The team builds today with 1.0.13 on AGP 9.3.3, but the newer artifacts are untested on that combination.

## Q6. Known bugs, limitations, performance and community complaints

### Takeaway
Most complaints are about silent failures: configurables not applying, Field or other widgets not appearing, and gamepad or telemetry not updating. Regressions keep showing up at SDK and season transitions. I found no published loop-time overhead measurements.

### Cited Findings
- **Silent configurable failure** with Sloth and Pedro 3 tuning on SDK 12. Fixed in 1.0.16 (see Q4). — [Issue #19](https://github.com/ftcontrol/ftcontrol-panels/issues/19)
- **BIOBUZZ Field missing in 1.0.13** (#18, 2026-09-22): "doesn't give a Field as an available panel selection". Users worked around it by pinning `com.bylazar:field:1.0.6` and adding the field PNGs as assets with a `FieldPluginConfig` override, or by dropping back to fullpanels 1.0.12. The maintainer said it was "fixed in 1.0.14". — [Issue #18](https://github.com/ftcontrol/ftcontrol-panels/issues/18)
- **Partial plugin failure** (#7, fullpanels 1.0.6, 2025-10-24): OpMode, battery and ping plugins worked, but gamepad, configurables and telemetry did not update. A user reported the same gamepad problem on v1.0.12 on 2026-07-09, and the issue is closed with no stated fix. — [Issue #7](https://github.com/ftcontrol/ftcontrol-panels/issues/7)
- **Limelight proxy crash** on robot restart (#12, 2025-11-25). Fixed 2025-12-06 ("fixed ll proxy crashing app"). — [Issue #12](https://github.com/ftcontrol/ftcontrol-panels/issues/12); [commits](https://github.com/ftcontrol/ftcontrol-panels/commits/main)
- **Duplicate telemetry output** when Panels telemetry also updates FTC telemetry (#11, 2025-11-19, closed same day). — [Issue #11](https://github.com/ftcontrol/ftcontrol-panels/issues/11)
- **DECODE field view broken** (#9, 2025-11-11, closed 2025-12-06). This is the 2025-26 season equivalent of #18. — [Issue #9](https://github.com/ftcontrol/ftcontrol-panels/issues/9)
- **Field API exposes an unimplemented feature.** `cursorHeading` and `setCursorHeading()` have no effect (#17, open since 2026-03-04, no response). — [Issue #17](https://github.com/ftcontrol/ftcontrol-panels/issues/17)
- **Telemetry widget limits.** A user writes that the Telemetry plugin "seems to mimic what happens on the Driver Station screen" and asks for selectable values and colored boolean views (#13, open). — [Issue #13](https://github.com/ftcontrol/ftcontrol-panels/issues/13)
- **Log noise:** 1.0.12 (2025-12-12) "Reduced Panels logs amount". PR #8 (closed 2026-06-17) proposed moving Logger.kt to Android logging. — [FullPanels changelog](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/FullPanels/web/config.ts); [PR #8](https://github.com/ftcontrol/ftcontrol-panels/pull/8)
- **Stale-asset upgrade problems:** the docs tell users to do a clean build or delete build folders "so old Panels assets are not reused". `AGENTS.md` notes plugins are found through packaged web assets, so "Kotlin classes alone do not make a plugin discoverable", and an AAR missing its frontend bundle makes the widget disappear. That was the mechanism behind the 1.0.13 Field bug. — [Prerequisites.svelte](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/Docs/web/src/docs/Prerequisites.svelte); [AGENTS.md](https://github.com/ftcontrol/ftcontrol-panels/blob/main/AGENTS.md)
- **Throughput control:** Telemetry 1.0.4 (2025-10-23) added `setMsTransmissionInterval` support. The Graph rewrite in Graph 1.0.3 (2025-10-08) came with commits "dynamic poll time" and "number parsing fix & fps improv". — [Telemetry changelog](https://github.com/ftcontrol/ftcontrol-panels/blob/main/library/Telemetry/web/config.ts); [commits](https://github.com/ftcontrol/ftcontrol-panels/commits/main)

### Inferences
- The project has broken at the start of the last two seasons: the DECODE field in November 2025 and the BIOBUZZ field plus SDK 12 and Sloth in September and October 2026. Both times, fixes arrived several weeks into the season. Teams should pin a known-good version and test after every upgrade.
- Several failures are silent: values that do not apply, or widgets that never appear. Teams should keep a quick on-robot check, such as echoing a configurable through telemetry.

### Gaps
- I found no published measurements of Panels' CPU, loop-time or network overhead on a Control Hub, and no head-to-head comparison with FTC Dashboard. An extended search turned up nothing quantitative.
- I found no Reddit (r/FTC), FTC forum or Chief Delphi threads about Panels. Discord discussion is not public or searchable.

## Q7. License and open-source status

### Takeaway
The source is public on GitHub under a custom "FTControl License v1.0". It is not an OSI open-source license: it bans commercial use and requires attribution for forks and derivatives. For an FTC team it is free to use.

### Cited Findings
- `LICENSE.md` contains "FTControl License v1.0, Copyright (c) 2025 bylazar.com". Forking, modifying and extending are allowed with attribution, which must include the original repo link and a statement "such as 'Based on FTControl bylazar.com'". The license states: "Commercial use of the Software is not permitted ... The Software may only be used for non-commercial purposes." Modified redistributions must keep the license. The software is provided "AS IS". — [LICENSE.md](https://github.com/ftcontrol/ftcontrol-panels/blob/main/LICENSE.md)
- GitHub reports the license as "Other" (`NOASSERTION`). — [ftcontrol/ftcontrol-panels](https://github.com/ftcontrol/ftcontrol-panels)
- The author's site footer says the content "may not be distributed without my permission". This applies to the website, not necessarily the code. — [bylazar.com project page](http://bylazar.com/projects/ftcontrol-panels-v1/)

### Inferences
- The source is available and forkable, so the team could patch or fork it if upstream stalls. That is how Dairy produced Sloth Panels. The non-commercial and attribution terms do not limit normal FTC team use, but the license is not MIT, BSD or Apache.

### Gaps
- None found.
