# Alternatives to Panels (other than FTC Dashboard) for live tuning, telemetry, visualization and logging on FTC robots, as of October 2026

Research date: 2026-10-14 (2026-27 BIOBUZZ season). The team's stack, read from its own build files: FTC SDK `12.0.0`, `com.bylazar:fullpanels:1.0.13`, NextFTC v2 (`dev.nextftc.v2:* 0.2.1`), Pedro `com.pedropathing:revhub:3.0.0` + `tuning:1.0.0` + `telemetry:1.0.0`, Ivy `com.pedropathing.ivy:pedro:1.1.1`, and the `dev.frozenmilk.sinister:Sloth:0.3.2` runtime library. The Sloth Load Gradle plugin is not applied, and stock Panels is used rather than the Sloth fork (source: local `build.dependencies.gradle` and `TeamCode/build.gradle` in this repo).

Note on naming: "BIOBUZZ" is the name of the 2026-27 FTC game, so other public repos called "biobuzz" (e.g. Mona-Shores-FTC-Robotics/biobuzz, cited below) belong to other teams. They are not this team's.

---

## Key question 0 (cross-cutting): What do the 2026-27 competition rules allow? This decides which tools matter at events

### Takeaway
The 2026-27 BIOBUZZ Competition Manual (version TU04), rule R704.D, says the FTC Driver Station app is the only allowed path for streaming telemetry and debug data. It names FTC Dashboard and FTControl Panels (Panels' former name) as prohibited "additional logging/streaming services". Any live dashboard therefore helps only in practice and in the pits. On-robot file logging that you review after the match is the only way to capture data from real matches.

### Cited Findings
- R704.D (2026-27 Competition Manual, version TU04, p.140): software on the Robot Controller Wi-Fi must limit continuous data streaming. Robot control data, debugging data and telemetry may be streamed only through the FTC Driver Station Application. Third-party logging/streaming services "such as FTC Dashboard, FTControl Panels, and others are prohibited." No continuous video stream is allowed. — [2026-2027 FTC Competition Manual, BIOBUZZ, TU04](https://ftc-resources.firstinspires.org/ftc/game/manual)
- R704.C (same manual): all programming laptops and other devices except the Driver Station must be disconnected from the Robot Controller Wi-Fi during MATCH play. — [2026-2027 FTC Competition Manual](https://ftc-resources.firstinspires.org/ftc/game/manual)
- E301 (same manual): teams may not set up their own Wi-Fi access points, ad-hoc networks or Bluetooth in the venue. — [2026-2027 FTC Competition Manual](https://ftc-resources.firstinspires.org/ftc/game/manual)
- The FateWeaver README warns that the 2026-2027 competition manual prohibits live streaming from third-party libraries during competition. It tells users to disable its `FlightProjector` live stream before competing. — [HermesFTC/FateWeaver](https://github.com/HermesFTC/FateWeaver)
- Historical: FTC Dashboard's own docs say it cannot be used during matches under RS09 (2021-22 Game Manual Part 1) but may be allowed in the pits. This is older rule wording. — [FTC Dashboard: Competition Use](https://acmerobotics.github.io/ftc-dashboard/competition.html) (search snippet only)

### Inferences
- R704.D's text is not limited to "during MATCH play" the way R704.C is. Read strictly, it may also restrict Panels-style streaming in the pits at events. Teams should check the official Q&A.
- Under R704.D, these are all practice-field tools: Panels, FTC Dashboard, AdvantageScope's live "FTC Dashboard" source, PsiKit's live server, FateWeaver's FlightProjector, AdvantageScope Lite served from the robot, and Pedro 3's robot-hosted AutoTune page. At events, the tools that matter are on-robot file loggers (KoalaLog, FateWeaver files, TRACE, a custom WPILOG writer) that you pull and review after the match. Their value to this team goes up a lot this season.
- Libraries that open a web server, such as Panels, must be configurable to stay off or quiet in competition builds. How Panels handles this was not verified (see Gaps).

### Gaps
- No official Q&A answer was found on whether R704.D applies in the pits or only on the field, or whether a library merely being installed but idle is acceptable.
- The TU04 publication date was not found in the extracted text.

---

## Key question 1: AdvantageScope and FTC in 2026, and the FTC logging libraries that feed it

### Takeaway
AdvantageScope has had experimental FTC support since v26.0.0-beta-1 (Sep 2025): FTC fields and robots, Road Runner logs, and a live "FTC Dashboard" source. The current stable release is v26.0.2 (25 Apr 2026). The project says it will officially support FTC only after the Systemcore transition in 2027-28. Several small community FTC loggers produce files AdvantageScope can open (PsiKit, KoalaLog, FateWeaver, TRACE, plus custom WPILOG writers). All are young, low-star projects maintained by individual students or mentors.

### Cited Findings
**AdvantageScope itself**
- The 2026 release added FTC fields and robot models to the 2D/3D field tabs, FTC coordinate-system options, Road Runner log file support, and support for the FTC Dashboard live streaming format. FTC support is labelled experimental and "still in active development", with a caution about using experimental software during the official season. — [AdvantageScope docs: What's New in 2026](https://docs.advantagescope.org/whats-new/)
- The same page says all AdvantageScope features will be officially supported in FTC after the transition to Systemcore in 2027-2028. — [What's New in 2026](https://docs.advantagescope.org/whats-new/)
- The AdvantageScope developers do not endorse any particular FTC logging solution. They note that third-party FTC libraries may produce WPILOG or RLOG and that unofficial solutions may cause compatibility issues. — [What's New in 2026](https://docs.advantagescope.org/whats-new/)
- Live sources supported: NetworkTables 4, NT4 (AdvantageKit), Phoenix Diagnostics, RLOG Server, and FTC Dashboard. The docs give no FTC Dashboard setup details (IP, port, or whether config values can be edited). — [AdvantageScope docs: Live Sources](https://docs.advantagescope.org/overview/live-sources/)
- Releases (years inferred from the page context):
  - v26.0.0-beta-1 (5 Sep 2025) added FTC fields/robots, Road Runner log support and FTC Dashboard support.
  - v26.0.0-beta-2 (5 Dec 2025) fixed FTC Dashboard live-mode pose fields and Road Runner parsing.
  - Stable releases: v26.0.0 (8 Jan 2026), v26.0.1 (28 Feb 2026), and v26.0.2 (25 Apr 2026, marked Latest).
  - 2027 alphas run up to v27.0.0-alpha-6 (23 Aug 2026).
  - No release note mentions Pedro Pathing, PsiKit or Panels.
  - Source: [AdvantageScope GitHub releases](https://github.com/Mechanical-Advantage/AdvantageScope/releases)
- File formats AdvantageScope reads: WPILOG, Hoot, REVLOG, Road Runner, CSV, NI DS log, and RLOG. — [Chief Delphi thread summary / AdvantageScope listing](https://www.chiefdelphi.com/t/advantage-scope-for-ftc/522762) (secondary, via search summary)
- According to search-result summaries, the default coordinate system for FTC fields from 2024-25 through 2026-27 is "Center/Rotated", with "Center/Red" (Systemcore) becoming the default for FTC in 2027-28. — [AdvantageScope docs: Coordinate Systems](https://docs.advantagescope.org/more-features/coordinate-systems/) (search snippet; page not fetched directly)
- A public FTC team repo found that AdvantageScope v27 requires struct-form poses because the old number-array form was removed. They validated their logs with v27.0.0-alpha-6's decoder. Their AdvantageScope export mirrors Pedro poses about a field centre of 70.75 in (141.5 in field), not 72 in. — [Mona-Shores-FTC-Robotics/biobuzz PR #150](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/pull/150) (closed draft, opened 1 Oct 2026; commits from 27 Sep 2026)

**AdvantageScope Lite for FTC (on-robot web build)**
- `page.j5155.AdvantageScope:lite:v26.0.0` is added as a TeamCode dependency and serves AdvantageScope in a browser at `http://192.168.43.1:8080/as/`. It connects to FTC Dashboard automatically when that is present. It highlights support for Road Runner and PsiKit logs, and needs an uploaded FTC asset zip for the field views. The repo has 27 commits, 6 stars, no releases, and is AGPL-3.0. — [j5155/AdvantageScope-Lite-FTC](https://github.com/j5155/AdvantageScope-Lite-FTC)

**PsiKit (AdvantageKit port for FTC)**
- An AdvantageKit port with the FRC dependencies removed. It claims about 90% of AdvantageScope features: timeline, graphs, 2D/3D field, tables, mechanism view.
  - Unsupported: swerve view, live tuning, log file review, joysticks.
  - Planned: live tuning, log files for review, log replay, Kotlin tooling.
  - Activity: 136 commits, 14 stars, 7 forks, no tagged releases. Maintainer is "Avery | 3825" on the Unofficial FTC Discord.
  - Source: [PsiLynx/PsiKit](https://github.com/psilynx/PsiKit/)
- Conflict: PsiKit's README lists "log file review" as unsupported, while AdvantageScope-Lite-FTC claims support for "PsiKit logs". Whether PsiKit writes match log files today is unresolved. — [PsiKit](https://github.com/psilynx/PsiKit/) vs [AdvantageScope-Lite-FTC](https://github.com/j5155/AdvantageScope-Lite-FTC)
- PsiKit's source comment and AdvantageScope's source disagree on which way Pedro +X points. — [biobuzz PR #150](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/pull/150)

**KoalaLog**
- An FTC logger that writes AdvantageScope-compatible `.wpilog` files. It uses an `@AutoLog` annotation processor (compile-time generated `Logged` implementations), `AutoLogManager`, and a `WpiLog` writer. It also ships ADB "LogPuller" executables to pull logs from the Control Hub. It is distributed via JitPack, but the README shows no version. Activity: 62 commits, 13 stars, no tagged releases. — [ori-coval/Koala-Log](https://github.com/ori-coval/Koala-Log)
- In July 2026 one Chief Delphi user said KoalaLog was "really helpful with analysis after match". KoalaLog's creator (Kully) recommended it in the same thread; the creator's own endorsement is not independent. — [Chief Delphi: Advantage Scope for FTC (21–27 Jul 2026)](https://www.chiefdelphi.com/t/advantage-scope-for-ftc/522762)
- The announcement thread asks for testers, which suggests early-stage status. — [Chief Delphi: Introducing KoalaLog](https://www.chiefdelphi.com/t/introducing-koalalog-an-ftc-logger-producing-advantagescope-compatible-wpilog-files-looking-for-feedback-and-testers/503204) (linked from the thread above; not fetched)

**FateWeaver (HermesFTC)**
- Records FTC robot data to its own `.fate` format (format v3), one file per OpMode. It needs an AdvantageScope build with FateWeaver v3 support.
  - Maven Central: `gay.zharel.fateweaver:ftc:<version>` (version not shown in the capture).
  - `FateRouter` adds routes to the SDK's NanoHTTPD server, with a robot-side page for downloading logs. `FlightProjector` streams to AdvantageScope over WebSocket, and the README says to disable it in competition.
  - Activity: 85 commits, 6 stars.
  - Source: [HermesFTC/FateWeaver](https://github.com/HermesFTC/FateWeaver)
- Its author (zach.waffle) promoted it on Chief Delphi in July 2026 and disclosed authorship. — [Chief Delphi thread](https://www.chiefdelphi.com/t/advantage-scope-for-ftc/522762)

**TRACE (The Allsparks, team 36117)**
- An FTC-first event and telemetry recorder: compact `.tlog` files on the hub, a desktop `TraceInspect --wpilog` converter to WPILOG 1.0, and an optional `trace-advantagescope` live module (off by default). Replay is designed but not implemented.
  - Version: `0.1.0-SNAPSHOT`. Not validated on Control Hub hardware. It calls itself "an initial public scaffold", with an audit dated 2026-08-17.
  - It evaluated PsiKit and did not adopt it as its core.
  - Source: [The-Allsparks/TRACE](https://github.com/The-Allsparks/TRACE)

**Custom WPILOG writer (peer example)**
- One 2026-27 team wrote its own dependency-free `WpiLogWriter` (WPILib Data Log 1.0) instead of using a library. It cited time lost in the prior season to logging libraries "it could not see inside". — [biobuzz PR #150](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/pull/150)

### Inferences
- AdvantageScope is the strongest post-match analysis and visualization tool available to FTC. It offers timeline scrubbing, synced graphs and 3D field replay, which Panels' Capture plugin does not match in depth. It is still a complement, not a Panels replacement: it does not log anything itself, its FTC live mode depends on FTC Dashboard's protocol (not Panels'), and none of the sources show live config editing for FTC.
- For this team, the most realistic AdvantageScope path is an on-robot WPILOG file logger, then pulling files over ADB or USB after matches and opening them in desktop AdvantageScope v26.0.2. The logger could be KoalaLog, TRACE, or a small custom writer. This path stays legal under R704.D because nothing streams.
- Pedro 3's `withLogger(Consumer<FollowerLog>)` hook (see Q4) is the natural attachment point for whichever logger is chosen.
- Every FTC logger found is maintained by one person or one team, has fewer than 15 stars and no tagged releases. Maintenance risk is high, so a thin custom writer is a defensible alternative.

### Gaps
- No exact last-commit dates were visible for PsiKit, KoalaLog, FateWeaver or AdvantageScope-Lite-FTC. The GitHub pages as captured showed commit counts but not dates.
- KoalaLog and FateWeaver version numbers were not found.
- It is unconfirmed whether AdvantageScope's FTC Dashboard live mode can edit `@Config` values. It is also unconfirmed whether AdvantageScope reads any Panels data at all. No source mentions Panels, so assume it does not.
- No dedicated AdvantageScope FTC setup page (e.g. a docs page for FTC) was found beyond "What's New in 2026".
- RLOG or NT4 bridges for FTC were not found as standalone projects. PsiKit serves live data to AdvantageScope, but its transport (RLOG vs NT4) was not stated in the README.

---

## Key question 2: Other FTC dashboards and telemetry tools (FTControl, Dairy/Sloth tools, Limelight, Driver Station / Driver Hub, Pedro AutoTune, Foxglove, Rerun, and anything new in 2025-26)

### Takeaway
Apart from FTC Dashboard, there is no independent, maintained all-in-one competitor to Panels. "FTControl" is Panels' former name, not a separate tool. Dairy Foundation offers Sloth-compatible forks of Panels and Dashboard, not its own dashboard. The new partial overlaps are Pedro 3's robot-hosted AutoTune page (drivetrain and localizer tuning) and Limelight's own web UI, which Panels already proxies. General robotics tools (Foxglove, Rerun) have no FTC integration that could be found.

### Cited Findings
- FTControl Panels is the earlier name of Panels, by Lazar (team 19234 ByteForce). The `ftcontrol-old` repo is an old release, and development moved to `ftcontrol-panels`. The old changelog ends at v0.6.8 (page last updated 2025-06-28) and says Panels 1.0.0 has released, with new docs at panels.bylazar.com. — [ftcontrol/ftcontrol-panels](https://github.com/ftcontrol/ftcontrol-panels); [FTControl changelog](https://ftcontrol.bylazar.com/docs/changelog/)
- The Panels site lists v1.0.18 as the latest version (no date shown); this team is on 1.0.13.
  - Plugins: Configurables, Telemetry (mirrors Driver Hub), Field, Graph, Capture (record/replay), Limelight (wireless access to the Limelight 3A dashboard, video, stats), Gamepad, Battery & Pinger, Lights, Themes and layouts, OpMode control, Camera Stream, and integrated docs.
  - Each plugin checks for updates when online. The site does not mention Sloth, AdvantageScope export, Pedro or NextFTC.
  - Source: [Panels (panels.bylazar.com)](https://panels.bylazar.com/)
- Limelight 3A's own configuration UI is served on port 5801 (e.g. `http://limelight.local:5801` over USB). Pipeline setup happens there. — [TRC Limelight Guide, BIOBUZZ](https://irp.cdn-website.com/3a1d1852/files/uploaded/TRC_Limelight_Guide_BIOBUZZ.pdf) (via search summary; not fetched)
- Pedro Pathing 3's tuning tool is AutoTune, described as a robot-hosted webpage for automatically tuning the drivetrain, localizer and the "Foresight" algorithm. The tuning and "What's new" pages do not mention Panels or FTC Dashboard and give no URL or port. — [Pedro docs: Tuning](https://pedropathing.com/docs/pathing/tuning); [What's new with Pedro 3](https://pedropathing.com/docs/pathing/pedro3)
- Pedro 3 adds built-in logging hooks. `Constants.create(hardwareMap).withLogger(Consumer<FollowerLog>)` receives a `FollowerLog` on every `follower.update()`. Multiple loggers are allowed. No file format or dashboard integration is defined; the user supplies the sink. — [Pedro docs: Logging](https://pedropathing.com/docs/pathing/reference/logging)
- NextFTC's site (v2) documents no telemetry, dashboard or logging tooling. It does not mention Panels, FTC Dashboard, AdvantageScope or Sloth. Its control section covers PID, feedforward, motion profiling and Kalman filtering. — [nextftc.dev](https://nextftc.dev/)
- FTC SDK v12.0 (12 Sep 2026) is the official 2026-27 release. Its release notes mention no telemetry, logging, dashboard or hot-reload changes. The prior versions were v11.2.1 (31 Jul) and v11.2 (15 Jul). — [FtcRobotController releases](https://github.com/FIRST-Tech-Challenge/FtcRobotController/releases)
- The official FTC SDK includes a CSV "Datalogger" teaching sample, which TRACE cites as inspiration for its CSV format. — [The-Allsparks/TRACE](https://github.com/The-Allsparks/TRACE)
- Under R704.D, Driver Station telemetry is the only sanctioned streaming channel in 2026-27. — [2026-2027 Competition Manual](https://ftc-resources.firstinspires.org/ftc/game/manual)
- Foxglove's SDK (C++, Python, Rust) streams live data or logs to MCAP. No FTC or Control Hub integration was found, and no FTC-related results were found for Rerun. — [Foxglove SDK docs](https://docs.foxglove.dev/docs/sdk)

### Inferences
- The only true Panels replacement is FTC Dashboard (covered separately). The remaining options are complements:
  - AdvantageScope plus a file logger, for analysis.
  - Pedro AutoTune, which overlaps Panels for Pedro tuning only.
  - Limelight's web UI, which Panels already wraps.
- Foxglove and Rerun would need a custom Java/Android bridge or a WPILOG/MCAP converter. They offer nothing for FTC that AdvantageScope doesn't already do with native FTC fields, so they are not a realistic choice for this team.
- Because Pedro 3 ships its own tuning UI, part of Panels' Pedro-tuning role may already be redundant. The team should check how `com.pedropathing:tuning:1.0.0` exposes AutoTune before relying on Panels for drivetrain tuning.

### Gaps
- Pedro AutoTune's URL/port, its dependency chain (the team's build comment suggests Pedro tuning uses the Sloth runtime), and whether it conflicts with Panels' port 8001 were not documented in the pages fetched.
- The Driver Hub's built-in web interface (Robot Controller Console / OnBot Java on port 8080) and the REV Hardware Client were not researched in depth. No 2025-26 telemetry features were found for either.
- The Panels 1.0.14–1.0.18 changelog and release dates were not found. The old changelog URL stops at 0.6.8, and `panels.bylazar.com/docs/changelog/` returned 404.
- A search summary claimed Dairy Foundation "maintains FTControl Panels". This was not supported by any primary source: Panels is by Lazar/ftcontrol, and Dairy publishes a Sloth fork. Treat that claim as incorrect or unverified.

---

## Key question 3: Hot-reload / fast-deploy (Sloth) and how it interacts with Panels

### Takeaway
Sloth (Dairy Foundation) is the current FTC hot-reload tool. It pushes only TeamCode in about 1–2 s, and its latest version is 0.3.2. Hot reload with Panels requires replacing stock Panels with Dairy's fork `com.bylazar.sloth:fullpanels:0.3.2+1.0.13`, which pins Panels at 1.0.13. One public team reports that `@Configurable` values may go stale after a hot reload (inferred, not confirmed). This team currently has only the Sloth runtime library, not the Load plugin or the Panels fork.

### Cited Findings
- Sloth is a hot code reload library for FTC. It uploads only TeamCode changes, usually in under about 2 s (often under 1), and changes persist across power cycles. It is also the Sinister runtime (classpath scanning and dynamic loading). `@Pinned` blocks dynamic changes. — [Dairy-Foundation/Sloth](https://github.com/Dairy-Foundation/Sloth)
- Setup per the README:
  - Add the repo `https://repo.dairy.foundation/releases`.
  - Gradle plugins: `dev.frozenmilk.teamcode` `12.0.0-1.2.2` and `dev.frozenmilk.sinister.sloth.load` `0.3.2` (Groovy: buildscript classpath `dev.frozenmilk:Load:0.3.2`).
  - Dependency: `dev.frozenmilk.sinister:Sloth:0.3.2`.
  - Do a full install first, then use the Gradle tasks `deploySloth` and `removeSlothRemote`.
  - Source: [Sloth README](https://github.com/Dairy-Foundation/Sloth)
- Dashboard compatibility: Sloth provides drop-in forks: `com.acmerobotics.slothboard:dashboard:0.3.2+0.6.0` for FTC Dashboard and `com.bylazar.sloth:fullpanels:0.3.2+1.0.13` for Panels, with individual Panels modules under `com.bylazar.sloth` at `0.3.2+<version>`. Libraries that depend on Dashboard should switch to `compileOnly`. — [Sloth README](https://github.com/Dairy-Foundation/Sloth)
- Limitations:
  - Only classes in `org.firstinspires.ftc.teamcode` and its subpackages hot-reload.
  - Library or dependency changes and `@Pinned` changes need a full install.
  - Leftover Sloth payloads can block normal installs. The fix is deleting `/storage/emulated/0/FIRST/dairy/sloth/*`.
  - Source: [Sloth README](https://github.com/Dairy-Foundation/Sloth)
- Repo activity: 49 commits, 61 stars, 13 forks, BSD-3-Clause-Clear, footer © 2026. No tagged releases were visible. — [Sloth](https://github.com/Dairy-Foundation/Sloth)
- Peer experience from a public 2026-27 team (merged 26 Sep 2026):
  - They suspect Panels keeps configurable fields per class loader, so after a Sloth hot reload the browser may edit a stale copy of a class that the OpMode no longer reads. They label this "inferred, not confirmed".
  - Their cure is a full TeamCode install, a power cycle and a page reload. They added a "Remove Sloth Remote" run config.
  - Source: [Mona-Shores-FTC-Robotics/biobuzz PR #70](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/pull/70)
- The same PR removed FTC Dashboard because with both Panels and Dashboard installed, the Robot Controller crash-looped on a `BindException` and never booted. This happened even with no team Java in the APK. — [biobuzz PR #70](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/pull/70)
- A third-party attempt to add Sloth to the official SDK repo (PR #2210, opened 1 Oct 2026, commit note "not tested") was auto-closed the same day; FIRST does not accept PRs to FtcRobotController. — [FtcRobotController PR #2210](https://github.com/FIRST-Tech-Challenge/FtcRobotController/pull/2210)
- Teams commonly hit setup friction: a missing Load plugin, a missing Panels fork dependency, or a per-laptop run-config module setting. — [Brookline-Robotics-Initiative/BBots2026-27 issue #4](https://github.com/Brookline-Robotics-Initiative/BBots2026-27/issues/4); [biobuzz PR #13](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/pull/13) (via search summary; not fetched individually)
- This team's `build.dependencies.gradle` includes `dev.frozenmilk.sinister:Sloth:0.3.2`, with a comment that it matches Pedro tuning's runtime version. `TeamCode/build.gradle` uses stock `com.bylazar:fullpanels:1.0.13`, and the Sloth Load plugin is not applied. — local repo files `build.dependencies.gradle`, `TeamCode/build.gradle`

### Inferences
- To get hot reload, the team would have to:
  1. Apply the Load plugin.
  2. Swap `com.bylazar:fullpanels:1.0.13` for `com.bylazar.sloth:fullpanels:0.3.2+1.0.13`.
  3. Accept being pinned to Panels 1.0.13 until Dairy publishes a newer fork. The upstream Panels is already at 1.0.18.
- Sloth mainly saves deploy time when iterating on logic. For pure constant-tuning, Panels `@Configurable` already avoids redeploys, so Sloth adds the most value for structural code changes (commands, Ivy routines, state machines).
- Avoid installing FTC Dashboard and Panels side by side. One team's evidence of a boot-time `BindException` is anecdotal but specific.
- Sloth affects deploy only and streams nothing, so R704.D does not restrict it. Any leftover Sloth payload on the hub should still be cleared before events to avoid stale-code surprises.

### Gaps
- No primary confirmation from Panels or Dairy of the stale-`@Configurable`-after-hot-reload behaviour was found.
- Sloth's last commit date and the publication date of 0.3.2 were not visible.
- It is unknown whether a `0.3.2+1.0.18` (or later) Sloth Panels fork exists. Only `+1.0.13` is documented in the README.

---

## Key question 4: Per-tool catalog: version and date, maintenance, setup, SDK 12.0 compatibility, coexistence with Panels, and fit with NextFTC v2 + Pedro 3

### Takeaway
None of the non-Dashboard tools fully replaces Panels' combination of live config, telemetry, field view and OpMode control. The best-fit additions for this stack are:
- An on-robot WPILOG logger fed from Pedro's `withLogger` hook and NextFTC subsystem state, viewed in desktop AdvantageScope v26.0.2.
- Optionally Sloth with the Panels fork, for faster iteration.

### Cited Findings
(Every row is sourced in Q1–Q3. The citations are repeated here for completeness.)

| Tool | Role | Latest version / date found | Maintenance signals | Setup effort | Works alongside Panels? | Fit with NextFTC v2 + Pedro 3 | Source |
|---|---|---|---|---|---|---|---|
| AdvantageScope (desktop) | Log viewer / analysis; live view via FTC Dashboard protocol | v26.0.2 stable (25 Apr 2026); v27.0.0-alpha-6 (23 Aug 2026) | Very active (Team 6328). FTC support experimental; official only from 2027-28 | Low (desktop install) + needs a logger on the robot | Yes. Separate desktop app; no Panels integration found | Library-agnostic. Pedro poses need mapping to FTC coordinates (centre 70.75 in per one team) | [releases](https://github.com/Mechanical-Advantage/AdvantageScope/releases), [what's new](https://docs.advantagescope.org/whats-new/), [PR #150](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/pull/150) |
| AdvantageScope Lite FTC | AdvantageScope served from the robot | `page.j5155.AdvantageScope:lite:v26.0.0` | 27 commits, 6 stars, no releases | Gradle dependency + asset zip upload | Unverified. It serves on `:8080/as/`, which is FTC Dashboard's port, not Panels' | Auto-connects to FTC Dashboard, so it is of limited use without Dashboard | [repo](https://github.com/j5155/AdvantageScope-Lite-FTC) |
| PsiKit | AdvantageKit-style logging + live server to AdvantageScope | No tagged release; 136 commits | 14 stars; single maintainer (team 3825) | Medium (docs/installing.md) | Not stated | No Pedro or NextFTC mention. Pedro +X mapping disputed | [repo](https://github.com/psilynx/PsiKit/), [PR #150](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/pull/150) |
| KoalaLog | `@AutoLog` → `.wpilog` file logger | No tagged release; JitPack; 62 commits | 13 stars; early-stage (testers requested) | Medium (annotation processor + ADB log puller) | Likely yes (file-only, no server) — inferred | Annotation-based; works with any Java/Kotlin code (Kotlin not stated) | [repo](https://github.com/ori-coval/Koala-Log), [CD thread](https://www.chiefdelphi.com/t/advantage-scope-for-ftc/522762) |
| FateWeaver | `.fate` file logger + optional WebSocket live stream | Maven Central `gay.zharel.fateweaver:ftc` (version not captured); 85 commits | 6 stars; author active on CD (Jul 2026) | Medium; needs an AdvantageScope build with FateWeaver v3 support | Not stated; it adds routes to the SDK's NanoHTTPD server | No Pedro or NextFTC mention | [repo](https://github.com/HermesFTC/FateWeaver) |
| TRACE | `.tlog` recorder + desktop WPILOG converter | `0.1.0-SNAPSHOT`; audit dated 2026-08-17 | "Initial public scaffold"; no hardware validation | Medium–high (clone/build; Android integration only sketched) | Likely yes (observational, no dashboard) — inferred | No Pedro or NextFTC mention | [repo](https://github.com/The-Allsparks/TRACE) |
| Sloth | Hot code reload | 0.3.2 (Panels fork `0.3.2+1.0.13`) | 49 commits, 61 stars; Dairy Foundation | Medium (Gradle plugin, run configs, fork swap) | Only via the Sloth Panels fork; possible stale `@Configurable` | Team already has the Sloth 0.3.2 runtime for Pedro tuning | [repo](https://github.com/Dairy-Foundation/Sloth), [PR #70](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/pull/70) |
| Pedro 3 AutoTune | Robot-hosted tuning page for drivetrain, localizer and Foresight | Pedro 3 (team uses revhub 3.0.0, tuning 1.0.0) | Official Pedro feature | Comes with Pedro tuning | Not stated | Native to the stack | [tuning](https://pedropathing.com/docs/pathing/tuning), [pedro3](https://pedropathing.com/docs/pathing/pedro3) |
| Pedro 3 `withLogger` | `FollowerLog` callback hook (no sink) | Pedro 3 | Official | Low; you write the sink | Yes (you can pipe into Panels telemetry or a file logger) | Native | [logging](https://pedropathing.com/docs/pathing/reference/logging) |
| Limelight web UI | Vision pipeline config | n/a | Vendor | Low | Panels already proxies it (Limelight plugin) | Independent | [Panels](https://panels.bylazar.com/), [TRC guide](https://irp.cdn-website.com/3a1d1852/files/uploaded/TRC_Limelight_Guide_BIOBUZZ.pdf) |
| Driver Station telemetry / SDK Datalogger sample | Only legal in-match telemetry; CSV logging sample | SDK v12.0 (12 Sep 2026) | Official | None / low | Yes (Panels Telemetry mirrors it) | Native | [SDK releases](https://github.com/FIRST-Tech-Challenge/FtcRobotController/releases), [manual R704.D](https://ftc-resources.firstinspires.org/ftc/game/manual) |
| Foxglove / Rerun | General robotics viz | n/a | Active but no FTC usage found | High (no Android/Java FTC bridge found) | n/a | Poor | [Foxglove SDK](https://docs.foxglove.dev/docs/sdk) |

- NextFTC v2's docs list no telemetry or tuning tooling. It is neutral: any of the above work alongside it via ordinary Java/Kotlin calls. — [nextftc.dev](https://nextftc.dev/)

### Inferences
- The lowest-risk upgrade path for this team is:
  1. Keep Panels for practice-field live tuning. Check whether moving from 1.0.13 to 1.0.18 is worthwhile; if Sloth is adopted, stay on the Sloth fork at 1.0.13.
  2. Add a file-based WPILOG logger. A thin custom writer or KoalaLog keeps dependencies small, which suits the high library churn.
  3. Feed the logger from Pedro's `withLogger` and from NextFTC subsystem state.
  4. Analyse matches in desktop AdvantageScope v26.0.2. Use v27 alpha only if struct-pose logs require it.
- "Likely coexists" for file-only loggers is an inference from their design (no web server). None of these projects documents Panels compatibility.

### Gaps
- No source documents a tested combination of Panels with PsiKit, KoalaLog, FateWeaver or TRACE.
- It is unknown whether any of these loggers has been tested on SDK 12.0 specifically. None states SDK version compatibility.

---

## Key question 5: Which tools are complements (logging/replay) and which are true replacements for Panels' live-config + telemetry + field-view role?

### Takeaway
- True replacements: only FTC Dashboard (covered separately). FTControl is just Panels' old name.
- Partial overlaps: Pedro 3 AutoTune (tuning only) and Limelight's UI (vision only).
- Complements: AdvantageScope, together with PsiKit, KoalaLog, FateWeaver, TRACE or a custom WPILOG writer, for logging, replay and analysis; and Sloth for deploy speed.

Given R704.D, the complements are what give this team usable data from real matches.

### Cited Findings
- Panels covers live config (Configurables), telemetry, field canvas, graph, capture/replay, OpMode control, Limelight proxy, gamepad and camera stream. — [Panels](https://panels.bylazar.com/)
- PsiKit explicitly lists "live tuning" as unsupported (planned). — [PsiKit](https://github.com/psilynx/PsiKit/)
- AdvantageScope is a viewer and cannot log by itself. Its FTC live source is FTC Dashboard's stream. — [Chief Delphi thread](https://www.chiefdelphi.com/t/advantage-scope-for-ftc/522762); [Live Sources](https://docs.advantagescope.org/overview/live-sources/)
- TRACE explicitly will not ship its own dashboard and treats AdvantageScope as its visualizer. — [TRACE](https://github.com/The-Allsparks/TRACE)
- Pedro 3's AutoTune is a robot-hosted tuning page limited to the drivetrain, localizer and Foresight. — [Pedro docs: Tuning](https://pedropathing.com/docs/pathing/tuning)
- R704.D prohibits third-party streaming services (naming FTC Dashboard and FTControl Panels) for 2026-27. — [2026-2027 Competition Manual](https://ftc-resources.firstinspires.org/ftc/game/manual)
- Peer data point: one 2026-27 team consolidated on Panels as its only dashboard (removing FTC Dashboard and its AdvantageScope live link) and separately built WPILOG export for AdvantageScope review. — [biobuzz PR #70](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/pull/70); [biobuzz PR #150](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/pull/150)

### Inferences
- Classification:
  - **Replace Panels:** FTC Dashboard only.
  - **Overlap part of Panels:** Pedro AutoTune and Limelight UI.
  - **Complement Panels:** AdvantageScope, AdvantageScope Lite, PsiKit, KoalaLog, FateWeaver, TRACE, custom WPILOG writer, Sloth, and the SDK Datalogger sample.
  - **Not relevant:** Foxglove and Rerun.
- No evidence was found that any non-Dashboard tool beats Panels at live `@Configurable` tuning for this stack. The gap Panels leaves is post-match analysis and in-match data capture, and AdvantageScope plus a file logger fills it.
- Since Panels and FTC Dashboard are both banned from streaming at events under R704.D, the choice between them matters less this season than adding a file logger.

### Gaps
- No direct head-to-head comparison of Panels Capture vs AdvantageScope replay quality was found.
- No sources from gm0, Reddit r/FTC or the FTC Discord specific to 2026 dashboard choices were retrieved; searches surfaced mainly GitHub and Chief Delphi.
