# Community adoption and sentiment: Panels vs FTC Dashboard vs alternatives (as of 2026-10-14, 2026-27 BIOBUZZ season)

Scope note: I gathered this research on 2026-10-14. "2025-26" means the DECODE season and is historical. "2026-27" means the current BIOBUZZ season (FTC SDK 12.0 was released 2026-09-07 and kickoff was 2026-09-12). I could not access Reddit, because the search tool blocks reddit.com, or Discord. Community sentiment therefore comes from GitHub issues and pull requests, team repos, and blogs.

Methodology for my own measurements. I used unauthenticated GitHub REST API calls and raw.githubusercontent.com file reads on 2026-10-14. GitHub code search needs authentication and was not available, so I measured with these proxies:
- **Fork-rate proxy.** I counted new forks of each official quickstart created after 2026-09-07 (the SDK 12 release).
- **Dependency scan of quickstart forks.** For every Pedro Quickstart fork created after 2026-09-07 that had at least one push after creation (n=137), and every Road Runner quickstart fork created after 2026-08-01 that had a push (n=25), I fetched `build.dependencies.gradle`, `TeamCode/build.gradle`, `TeamCode/build.gradle.kts` and the root `build.gradle` from the default branch. I then grepped them for `com.bylazar` (Panels, including the `com.bylazar.sloth` variant), `com.acmerobotics.dashboard` (FTC Dashboard), `fateweaver`, `dev.nextftc`, `com.pedropathing.ivy`, solverslib, `com.pedropathing:tuning`, `com.pedropathing:ftc:2` (Pedro 2), `com.pedropathing:revhub:3` (Pedro 3) and roadrunner.
- **Season-wide sample.** I ran the GitHub repository search `biobuzz created:>2026-07-01 fork:true`, which returned 1,144 repos in total. I took the first 500 by best-match ranking and ran the same dependency scan. 358 of the 500 had FTC Gradle build files at the expected root paths; the others were non-FTC or used other layouts and were excluded.
- **Limits.** The sample covers only public repos, only repos that mention "biobuzz", and only build files at the root/TeamCode level. It is early in the season (many repos are still fresh forks), and declaring a dependency does not prove a team actively uses it.

---

## Q1. What do the official quickstarts and docs default to or recommend (2025-26 vs 2026-27)?

### Takeaway
In 2025-26, Pedro Pathing's quickstart shipped Panels by default and its docs called Panels "fully supported". For 2026-27, Pedro 3.0.0 (released 2026-09-10) removed Panels from the quickstart: it now ships only Pedro's own robot-hosted "AutoTune" webpage, and the Pedro docs no longer mention Panels at all. Road Runner still defaults to FTC Dashboard, and its quickstart has not been updated to SDK 12. NextFTC v2's docs reference FTC Dashboard and FateWeaver/AdvantageScope, not Panels. SolversLib and the FTC SDK recommend no dashboard.

### Cited Findings
**Pedro Pathing, historical 2025-26**
- The old Pedro docs page "Choosing a Dashboard" said: "You may use either Panels or the FTC Dashboard. The quickstart comes with Panels installed by default." It described Panels as "fully supported by Pedro Pathing". It said FTC Dashboard, "because of the complexity of Pedro Pathing's constants, … does not support live tuning of Pedro Pathing." — [Pedro Docs repo, dashboard.mdx at commit ca0f201 (Nov 2025 version)](https://raw.githubusercontent.com/Pedro-Pathing/Docs/ca0f2017ba757ea761a455384506d49e6e89bd43/content/docs/pathing/dashboard.mdx)
- The last 2025-26 quickstart (commit 4d160da, 2026-05-08, Pedro 2.1.2) declared `com.bylazar:fullpanels:1.0.12` and `com.pedropathing:telemetry:1.0.0`, with the `mymaven.bylazar.com` repository. — [Pedro Quickstart build.dependencies.gradle @4d160da](https://raw.githubusercontent.com/Pedro-Pathing/Quickstart/4d160da/build.dependencies.gradle)

**Pedro Pathing, how the 2026-27 change happened**
- During Pedro 3 development (commits from 2026-05-26 onward), the quickstart temporarily used `com.acmerobotics.slothboard:dashboard:0.2.4+0.4.17`, a Sloth build of FTC Dashboard, and later `com.acmerobotics.dashboard:dashboard:0.6.0` (commits 199aa01, 2026-05-27, and 46a3283, 2026-06-15). — [Pedro Quickstart @1426451](https://raw.githubusercontent.com/Pedro-Pathing/Quickstart/1426451/build.dependencies.gradle); [@199aa01](https://raw.githubusercontent.com/Pedro-Pathing/Quickstart/199aa01/build.dependencies.gradle); [commit history](https://github.com/Pedro-Pathing/Quickstart/commits/master/build.dependencies.gradle)

**Pedro Pathing, current 2026-27 state**
- The current quickstart (master, last push 2026-10-08) declares only FTC SDK 12.0.0, `com.pedropathing:revhub:3.0.1` and `com.pedropathing:tuning:1.0.1`. It has no `com.bylazar` and no `com.acmerobotics` dependency, and the TeamCode sources import only `com.pedropathing.*` packages. — [Pedro Quickstart build.dependencies.gradle (master)](https://raw.githubusercontent.com/Pedro-Pathing/Quickstart/master/build.dependencies.gradle); [Pedro Quickstart repo](https://github.com/Pedro-Pathing/Quickstart)
- The repo description still reads "Quickstart for PedroPathing and Panels integration". It was not updated when Panels was removed. — [Pedro Quickstart repo](https://github.com/Pedro-Pathing/Quickstart)
- `com.pedropathing:tuning:1.0.1` depends on `dev.frozenmilk.sinister:Sloth:0.3.2`, `org.nanohttpd:nanohttpd-websocket:2.3.1` and jnanoid. It does not depend on Panels: it runs its own embedded web server. — [Maven Central POM tuning-1.0.1](https://repo1.maven.org/maven2/com/pedropathing/tuning/1.0.1/tuning-1.0.1.pom)
- Pedro's docs: "Pedro Pathing utilizes AutoTune, a robot-hosted webpage that allows for easy tuning of your robot's drivetrain, localizer, and the Foresight algorithm." — [pedropathing.com Tuning](https://pedropathing.com/docs/pathing/tuning) (source: [Docs repo tuning/index.mdx](https://github.com/Pedro-Pathing/Docs/blob/master/content/docs/pathing/tuning/index.mdx))
- The "What's new with Pedro 3" page lists AutoTune ("a robot-hosted webpage that allows for automatic tuning"). It also lists built-in logging via `Consumer<FollowerLog>` loggers, whose example writes to the SDK `telemetry`. It does not mention any dashboard. — [pedropathing.com Pedro 3](https://pedropathing.com/docs/pathing/pedro3); [Logging reference](https://pedropathing.com/docs/pathing/reference/logging)
- The "Choosing a Dashboard" page now returns 404. It was last modified in Docs commit "plane work" on 2026-08-05, and a grep of the current Docs repo finds no "Panels" or "dashboard" mentions in the pathing or Ivy docs. — [pedropathing.com/docs/pathing/dashboard (404)](https://pedropathing.com/docs/pathing/dashboard); [Docs commit history for dashboard.mdx](https://github.com/Pedro-Pathing/Docs/commits/master/content/docs/pathing/dashboard.mdx)

**Road Runner (ACME Robotics)**
- The quickstart's TeamCode build declares `com.acmerobotics.dashboard:dashboard:0.5.1`. It is still on FTC SDK 11.0.0, and its last commit was 2025-10-31 ("Bump dash"). There was no SDK 12 merge as of 2026-10-14. — [road-runner-quickstart TeamCode/build.gradle](https://raw.githubusercontent.com/acmerobotics/road-runner-quickstart/master/TeamCode/build.gradle); [road-runner-quickstart repo](https://github.com/acmerobotics/road-runner-quickstart)
- The Road Runner v1.0 installation docs still point to the quickstart, which bundles "FTC Dashboard, and tuning utilities", and show `com.acmerobotics.dashboard:dashboard:0.5.1`. — [rr.brott.dev installation](https://rr.brott.dev/docs/v1-0/installation/)

**NextFTC**
- The v2 docs (2026-27; repo pushed 2026-10-06) Telemetry page says: "`Telemetry` lets you send data to the Driver Station, FTC Dashboard, or anywhere else." The example backend is `FtcDashboard.getInstance().telemetry`. The page also documents FateWeaver (HermesFTC) logging, which is viewable at `192.168.43.1:8080/fate` and "openable in AdvantageScope". Panels is not mentioned. — [NextFTC-Docs telemetry.mdx](https://github.com/NextFTC/NextFTC-Docs/blob/main/src/content/docs/robot/telemetry.mdx)
- The NextFTC Extensions repo has `fateweaver`, `pedro` and `roadrunner` modules, but no Panels module. — [NextFTC/Extensions](https://github.com/NextFTC/Extensions)
- Historical: the v1-era NextFTC Quickstart included `com.acmerobotics.dashboard:dashboard:0.4.16` with the comment "Remove if you don't intend to use the FTC Dashboard". The v1 docs page "Configuring with the FTC Dashboard" says "It is common to tune your controllers with the FTC Dashboard." — [NextFTC Quickstart build.dependencies.gradle](https://github.com/NextFTC/Quickstart/blob/main/build.dependencies.gradle); [NextFTC-Docs-v1 control/dashboard.md](https://github.com/NextFTC/NextFTC-Docs-v1/blob/HEAD/src/control/dashboard.md)
- The NextFTC v2 `robot` module is "built on PedroPathing's Ivy scheduler". — [NextFTC/NextFTCSuite README](https://github.com/NextFTC/NextFTCSuite)

**SolversLib (FTCLib fork)**
- Neither the installation page (current `org.solverslib:core:0.3.6` plus the optional `pedroPathing` module) nor the docs index (llms.txt) mentions Panels or any dashboard: 0 matches for "panel" or "dashboard". — [SolversLib installation](https://docs.seattlesolvers.com/installation.md); [SolversLib llms.txt](https://docs.seattlesolvers.com/llms.txt)

**FTC SDK**
- The SDK 12.0 release notes (20260907) contain no dashboard features. The only telemetry-related item is a fix for DS battery voltage not updating when no telemetry was sent. — [SDK 12.0 notes as reproduced in the Pedro Quickstart README](https://github.com/Pedro-Pathing/Quickstart/blob/master/README.md); [FtcRobotController releases](https://github.com/FIRST-Tech-Challenge/FtcRobotController/releases)

### Inferences
- For 2026-27, no major library quickstart defaults to Panels any more. The only quickstart that ever did (Pedro) dropped it in favour of its own AutoTune web page. FTC Dashboard remains the default only for Road Runner, whose quickstart appears unmaintained this season.
- Pedro's move away from Panels looks like a product decision (in-house AutoTune) more than a judgement against Panels. During development, Pedro briefly used FTC Dashboard and Slothboard before settling on neither.
- Biobuzz 26507's stack (Pedro 3 + Ivy + NextFTC v2 + Panels) no longer matches any official quickstart default. Panels is now a team-chosen add-on.

### Gaps
- I could not find a written statement from the Pedro maintainers explaining why Panels was removed (no changelog line or docs note). That discussion probably happened on the Pedro Discord, which I could not access.
- I did not check FTCLib's own (legacy) docs, and I did not identify which port AutoTune's web server uses.

---

## Q2. Adoption evidence: who uses what?

### Takeaway
In the 2026-27 public-repo sample, Panels appears in about 27% of FTC repos and FTC Dashboard in about 21%. A majority (about 59%) declares neither. Panels adoption sits almost entirely inside the Pedro ecosystem, and it is collapsing there as teams move to Pedro 3: about 93% of repos still on Pedro 2 declare Panels, versus about 34% of Pedro 3 repos. Pedro is by far the most-forked path-following quickstart this season.

### Cited Findings
- **Repo-level stats (GitHub API, 2026-10-14):**
  - Panels (`ftcontrol/ftcontrol-panels`): 40 stars, 10 forks, created 2025-07-08, last push 2026-10-07.
  - FTC Dashboard (`acmerobotics/ftc-dashboard`): 218 stars, 200 forks, created 2018-05-19, last push 2026-09-07.
  - Sources: [Panels repo](https://github.com/ftcontrol/ftcontrol-panels); [API](https://api.github.com/repos/ftcontrol/ftcontrol-panels); [FTC Dashboard repo](https://github.com/acmerobotics/ftc-dashboard); [API](https://api.github.com/repos/acmerobotics/ftc-dashboard)
- **Quickstart forks:**
  - Pedro Quickstart: 681 total forks; 197 new forks created 2026-09-07 to 2026-10-14, 137 of them with pushes.
  - Road Runner quickstart: 1,738 total forks accumulated since about 2023, but only 24 new forks since 2026-09-07 (36 since 2026-08-01).
  - Sources: [Pedro forks API](https://api.github.com/repos/Pedro-Pathing/Quickstart/forks?sort=newest&per_page=100); [RR forks API](https://api.github.com/repos/acmerobotics/road-runner-quickstart/forks?sort=newest&per_page=100)
- **Scan of the 137 active Pedro Quickstart forks (2026-27):**

  | Flag | Repos |
  |---|---|
  | Pedro 3 tuning | 132 |
  | Panels | 23 (17%) |
  | FTC Dashboard | 14 (10%) |
  | Both | 4 |
  | Neither | 104 (76%) |
  | Ivy | 36 |
  | NextFTC | 8 |
  | SolversLib | 7 |

  Examples with Panels added back: PC-Robotics/10088-BIOBUZZ, Barrington-FTC/10847-Biobuzz-Omar, botsandbytes/biobuzz (Panels and Dash). Examples with FTC Dashboard: Team-19824-Pride-Robotics/FlyCode26-27, FTC-12459-Gibraltar-Jedi/Gibraltar-Jedi---BIOBUZZ. — [Pedro Quickstart forks](https://github.com/Pedro-Pathing/Quickstart/forks) (my own scan, methodology above)
- **Scan of the 25 active Road Runner quickstart forks since 2026-08-01:** 25/25 declare FTC Dashboard, 0 declare Panels. Examples: FTC-wildcircuits/WildCircuits-BIOBUZZ-RR, 9880AfterShock/2027-Biobuzz, lexrobotics/2027-4029-Odometry-RRv1.0. — [RR quickstart forks](https://github.com/acmerobotics/road-runner-quickstart/forks) (my own scan)
- **Season-wide sample** (358 FTC repos from the 500 best-match results of [GitHub repo search "biobuzz created:>2026-07-01"](https://api.github.com/search/repositories?q=biobuzz+created:%3E2026-07-01+fork:true); 1,144 total hits):

  | Group | n | Panels | FTC Dashboard | Both | Neither |
  |---|---|---|---|---|---|
  | All FTC repos | 358 | 96 (27%) | 76 (21%) | 27 | 213 (59%) |
  | Any Pedro | 206 (58%) | 93 (45%) | 43 (21%) | 26 | — |
  | Pedro 3 | 163 | 55 (34%) | 33 (20%) | 17 | — |
  | Pedro 2 (carried over from last season) | 42 | 39 (93%) | 11 | 9 | — |
  | Road Runner | 32 | 3 | 32 (100%) | — | — |
  | Non-Pedro | 152 | 3 (2%) | 33 (22%) | — | — |
  | NextFTC (v2 coordinates) | 23 | 11 | 7 | — | — |
  | Ivy | 61 | 28 | 18 | — | — |
  | SolversLib | 41 | 27 | 14 | — | — |
  | FateWeaver | 1 | — | — | — | — |

  — (my own scan; repo list from the search link above)
- **Third-party quickstarts that re-add Panels exist.** One example is "Pedro Pathing Quickstart integrated with Lumiere, FTC SDK 12, Ivy, Panels, AutoTune, and desktop simulation" (search-result description; I did not open the repo). — [Dillylol/Lumiere-Quickstart](https://github.com/Dillylol/Lumiere-Quickstart)
- **Historical 2025-26 team-level example.** One team opened an issue saying Panels was "interesting and perhaps better" than FTC Dashboard and suggested running both to compare (from the search-result snippet; date not verified). — [ftc16221/vigilant-spork issue #28](https://github.com/ftc16221/vigilant-spork/issues/28)

### Inferences
- **Panels' popularity was largely default-driven.** Teams that inherited the Pedro 2 quickstart almost all have it (93%). Teams that started from the Pedro 3 quickstart mostly don't (17% of quickstart forks, 34% of the broader Pedro 3 sample). Outside Pedro, Panels is close to absent (2%).
- **FTC Dashboard's share is steadier** (about 20–22%) because Road Runner users and non-Pedro teams use it. With Road Runner forks falling sharply this season, though, its base is not growing.
- **"Neither" is the plurality this season**, about 59% overall and 76% of fresh Pedro forks. Many teams appear to rely on Driver Station telemetry plus Pedro's AutoTune page. Some of this reflects how early in the season it is; teams may add a dashboard later.
- **Panels is still a mainstream choice among teams that add a dashboard on top of Pedro 3**: 55 Panels versus 33 FTC Dashboard in the Pedro 3 sample. It is no longer the default, however.

### Gaps
- I found no reliable data on which dashboards top teams use: Worlds 2026 (DECODE) winners, Premier Event teams, or portfolios. Repos for the 2026 winning-alliance teams (21087, 30030, 11228) were not found by search ([Edupedu report on winners](https://www.edupedu.ro/echipa-de-robotica-de-la-colegiul-national-gheorghe-murgoci-din-braila-a-castigat-campionatul-mondial-first-tech-challenge-2026-iar-echipa-colegiului-national-b-p-hasdeu-din-buzau-este-vicecampioana/)).
- Download statistics for the Maven artifacts (mymaven.bylazar.com, maven.brott.dev, Maven Central) are not public.
- GitHub code search, which would give exact counts of `com.bylazar` vs `com.acmerobotics.dashboard` across all FTC repos, needs authentication and was not available. The figures above are samples.

---

## Q3. Community sentiment: praise and complaints

### Takeaway
Public praise for Panels centres on its richer feature set (configurables, capture/replay, plugins, OpMode control, Limelight proxy) and its former first-class Pedro support. The main 2026-27 complaint is a silent breakage: Panels Configurables stopped updating when combined with Pedro 3's Sloth-based AutoTune. That was diagnosed by the community and fixed by Panels about 11 days after the first report. FTC Dashboard draws few public complaints, but it lags on season updates. Reddit, Discord and YouTube sentiment could not be accessed.

### Cited Findings
**Panels issue #19 (opened 2026-09-22)**
- Title: "Setting the value of @Configurable variables does not seem to be working". It had 22 comments.
- Several users on SDK 12 reported that configurables had no effect and that the BIOBUZZ field image did not work (one says the "biobuzz 1.0.7 field does NOT work, must be downgraded to 1.0.6").
- A user diagnosed: "com.pedropathing:tuning bundles Sloth, and Sloth silently breaks Panels Configurables by making a second copy of all classes."
- Panels author MonsieurLazar closed it on 2026-10-03: "Panels works with sloth now thanks to Oscar's PR."
- Source: [ftcontrol-panels issue #19](https://github.com/ftcontrol/ftcontrol-panels/issues/19)

**Pedro issue #188 (2026-09-29)**
- It reported the same incompatibility. Pedro org member BeepBot99 replied: "You need to use Sloth Panels instead of FTControl Panels."
- A contributor asked that "this information is posted loudly in the documentation somewhere. panels config not working is silent."
- Source: [PedroPathing issue #188](https://github.com/Pedro-Pathing/PedroPathing/issues/188)

**A Pedro contributor's framing**
- zachwaffle4 said the problem is "not with Pedro Pathing itself, but with the web tuning module used by the Pedro 3 quickstart". Once tuning is complete, teams "can remove that dependency (and delete your `Tuning.java` file)". — [ftcontrol-panels issue #19](https://github.com/ftcontrol/ftcontrol-panels/issues/19)

**Panels issue history (2025-08 to 2026-10)**
- There are 20 issues and PRs in total; 4 are open.
- 2025-26 issues included:
  - "Decode field view is broken" (#9)
  - "Limelight proxy plugin causing crash on robot restart" (#12)
  - "Some parts of panels not working?" (#7)
  - "Duplicate telemetry output…" (#11)
- Several were closed in a batch on 2025-12-06.
- PR #15 "Sloth & Other improvements" by Froze-N-Milk was opened 2025-12-26 and only closed or merged on 2026-10-03, after the Sloth breakage became widespread.
- Source: [ftcontrol-panels issues](https://github.com/ftcontrol/ftcontrol-panels/issues?q=is%3Aissue) (via GitHub API listing)

**Mona Shores PR #70 (2026-09-26): one team's switch to Panels only**
- Mona Shores FTC (2026-27) removed FTC Dashboard and kept only Panels. Its reason: with both installed, "the Robot Controller crash-loops on `BindException` and never boots."
- The PR also lists Panels rough edges:
  - Values changed in code didn't appear in Panels until `refreshClass` was called.
  - Limelight `pipelineIndex` edits did nothing until restart.
  - A suspected stale config after Sloth Load hot reload ("inferred, not confirmed").
- It also dropped AdvantageScope, which was reading Dashboard's stream.
- Single-team report; note that 27 repos in my sample declare both dashboards, so the conflict may depend on version or configuration.
- Source: [Mona-Shores-FTC-Robotics/biobuzz PR #70](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/pull/70)

**Mona Shores PR #12: Panels coordinates in a Pedro 3 migration**
- The same team's migration moved Panels from `com.bylazar.sloth:fullpanels:0.2.4+1.0.12` to `0.3.2+1.0.13`, the Sloth variant of Panels. — [Mona Shores PR #12](https://github.com/Mona-Shores-FTC-Robotics/biobuzz/pull/12) (from search snippet)

**Panels author's positioning**
- Panels was created "as a response to outdated tools, clunky workflows, and limited visibility" (search-result paraphrase of the launch post). — [bylazar.com: Introducing Panels](https://bylazar.com/blog/introducing-panels-ftc-dashboard/)
- Panels' own comparison table (as summarised in search results; I did not fetch it directly):
  - Panels has "Full support" for Capture, which FTC Dashboard lacks.
  - Panels has plugins and more configurable data types.
  - FTC Dashboard has the more featured field view.
  - Source: [FTControl docs overview](https://ftcontrol.bylazar.com/docs/overview/)

**FTC Dashboard: no public complaint threads found**
- The README notes gamepad support is "volatile due to unstable browser APIs". — [ftc-dashboard repo](https://github.com/acmerobotics/ftc-dashboard)

### Inferences
- Panels is responsive but fragile at ecosystem seams (Sloth, season field images, SDK bumps). Issues tend to be fixed when they become widespread, with roughly 1–2 week turnaround in Sept–Oct 2026, though some PRs sat for months.
- For Biobuzz 26507 specifically: the repo pins `com.bylazar:fullpanels:1.0.13` alongside `dev.frozenmilk.sinister:Sloth:0.3.2` and `com.pedropathing:tuning:1.0.0`. That is exactly the combination reported to silently break Configurables. Options:
  - Upgrade to a Panels release after 2026-10-03; the latest `com.bylazar:fullpanels` is 1.0.18, published 2026-10-07 per [Maven metadata](https://mymaven.bylazar.com/releases/com/bylazar/fullpanels/maven-metadata.xml).
  - Switch to Sloth Panels, `com.bylazar.sloth:fullpanels`, latest `0.3.2+1.0.13` on [Dairy Maven](https://repo.dairy.foundation/releases/com/bylazar/sloth/fullpanels/maven-metadata.xml).
  - Drop `com.pedropathing:tuning` after tuning is done.

  The exact fullpanels version that contains the fix is unverified.

### Gaps
- I could not access r/FTC, because reddit.com is blocked for the search and fetch tools, or the FTC, Pedro or NextFTC Discords. The search engine surfaced no YouTube tutorials comparing Panels and FTC Dashboard for 2026-27.
- I found no maintainer-to-maintainer public commentary, such as rbrott on Panels or Lazar on FTC Dashboard.

---

## Q4. Any abandonment, deprecation, merger or supersession in 2026? Relationships between maintainers?

### Takeaway
Neither tool is abandoned or deprecated as of 2026-10-14. Panels is very actively developed: 19 releases of 1.0.x, the latest on 2026-10-07, with Sloth/Dairy integration merged on 2026-10-03. FTC Dashboard is maintained but slower: v0.6.0 came out 2026-05-16 and the last commits were in Sept 2026, with no SDK 12 merge and no BIOBUZZ field image release yet. Panels has been partly superseded within Pedro by Pedro's own AutoTune, but there is no formal deprecation. Panels and the Pedro/NextFTC/Dairy maintainers are clearly intertwined socially and in code, but there is no shared GitHub org or formal partnership.

### Cited Findings
**Panels release activity**
- `com.bylazar:fullpanels` versions 1.0.0 through 1.0.18; the metadata `lastUpdated` is 2026-10-07. — [Panels Maven metadata](https://mymaven.bylazar.com/releases/com/bylazar/fullpanels/maven-metadata.xml)
- Recent commits:
  - 2026-10-07: "fix(docs): require Kotlin 2.4.10 for consumers; release Docs 1.0.9 and FullPanel"
  - 2026-10-03: "merge(main): integrate Dairy and Sloth support"
  - Not archived; created 2025-07-08.
  - Source: [ftcontrol-panels commits (API)](https://api.github.com/repos/ftcontrol/ftcontrol-panels/commits)
- Contributors: MonsieurLazar (429 commits) and Froze-N-Milk (21). This is effectively a one-maintainer project. — [Panels contributors](https://github.com/ftcontrol/ftcontrol-panels/graphs/contributors)

**FTC Dashboard release activity**
- Releases:
  - v0.6.0 (2026-05-16): Limelight 3A proxy and view, gamepad view, robot configuration editor, "Updated to SDK v11".
  - v0.5.1 (2025-10-31)
  - v0.5.0 (2025-09-09): DECODE field
  - Source: [ftc-dashboard releases](https://github.com/acmerobotics/ftc-dashboard/releases); [API](https://api.github.com/repos/acmerobotics/ftc-dashboard/releases)
- Commits after v0.6.0 are maintenance only, by rbrott:
  - 2026-08-22: a keyboard-gamepad fix
  - 2026-09-07: lint/CI, nix flake, test-server task
  - There is no SDK 12 merge and no BIOBUZZ field image. Most 2025-26 feature work came from community contributors (mxtmx, GraysonTinker/WHS Robotics and others).
  - Source: [ftc-dashboard commits](https://github.com/acmerobotics/ftc-dashboard/commits/master)

**Pedro supersession, with no deprecation notice**
- The Pedro 3 quickstart and docs replaced Panels-based tuning with AutoTune and removed the dashboard page (see Q1). — [Pedro Quickstart](https://github.com/Pedro-Pathing/Quickstart); [Pedro Docs commits](https://github.com/Pedro-Pathing/Docs/commits/master/content/docs/pathing/dashboard.mdx)
- No source states that Pedro deprecated Panels. Pedro maintainers still point users to "Sloth Panels" for compatibility. — [PedroPathing issue #188](https://github.com/Pedro-Pathing/PedroPathing/issues/188)

**Relationships**
- The Pedro-Pathing org has a fork of ftcontrol-panels, `Pedro-Pathing/Panels`, created 2025-08-17 with last push 2025-08-19. — [Pedro-Pathing/Panels](https://github.com/Pedro-Pathing/Panels)
- Lazar (MonsieurLazar) has 7 commits in the PedroPathing repo. — [PedroPathing contributors](https://github.com/Pedro-Pathing/PedroPathing/graphs/contributors)
- Lazar is credited as "Lazar of FTC team 19234 ByteForce"; the Panels repo is owned by the separate `ftcontrol` org. — [ftcontrol-panels](https://github.com/ftcontrol/ftcontrol-panels)
- Froze-N-Milk ("Oscar", the Dairy Foundation/Sloth author) contributed the Sloth support to Panels and 4 commits to Pedro. — [Panels issue #19](https://github.com/ftcontrol/ftcontrol-panels/issues/19); [PedroPathing contributors](https://github.com/Pedro-Pathing/PedroPathing/graphs/contributors)
- Pedro-Pathing public org members are BaronClaps, BeepBot99, DylanBPY, junkjunk123 and Theo-928. rowan-mcalpin (the NextFTC creator) has 15 commits in PedroPathing. — [Pedro-Pathing org](https://github.com/Pedro-Pathing); [PedroPathing contributors](https://github.com/Pedro-Pathing/PedroPathing/graphs/contributors)
- NextFTC v2's robot module is built on Pedro's Ivy scheduler. — [NextFTCSuite](https://github.com/NextFTC/NextFTCSuite)

**Other alternatives seen**
- FateWeaver (HermesFTC, a RoadRunner-log-based logger, AdvantageScope-viewable) is integrated into NextFTC v2 docs and Extensions. Its repo was pushed 2026-10-11, but only 1 of 358 sampled repos uses it. — [HermesFTC/FateWeaver](https://github.com/HermesFTC/FateWeaver); [NextFTC telemetry docs](https://github.com/NextFTC/NextFTC-Docs/blob/main/src/content/docs/robot/telemetry.mdx)
- The AdvantageScope docs list "FTC Dashboard streaming" as a live data source. — [docs.advantagescope.org](https://docs.advantagescope.org/)

### Inferences
- The realistic threat to Panels is not abandonment. It is loss of its distribution channel: Pedro's quickstart used to install it for every new Pedro team.
- FTC Dashboard's slow season update (no SDK 12 or BIOBUZZ release by mid-October), combined with a Road Runner quickstart frozen at SDK 11, suggests the ACME ecosystem is in maintenance mode. That is an inference from activity data, not a stated status.
- The close Pedro, NextFTC and Dairy/Sloth contributor overlap means Panels compatibility problems tend to be fixed quickly once surfaced. There is no guarantee, because there is no formal partnership.

### Gaps
- I found no official announcement or post from Lazar about Panels' 2026-27 roadmap.
- I found no public statement from rbrott/ACME on FTC Dashboard's maintenance status.
- I could not verify BeepBot99's NextFTC role: the GitHub API was rate-limited at the time, and the NextFTCSuite page showed no contributors.

---

## Q5. FIRST rules or hardware changes for 2026-27 that affect dashboard use

### Takeaway
This is the most significant change found. The 2026-27 BIOBUZZ Competition Manual (TU04) rule R704.D explicitly prohibits third-party logging and streaming services, naming "FTC Dashboard, FTControl Panels, and others". The 2025-26 manual's bandwidth rule (R706) named no tools. R704.C also requires laptops to be disconnected from the robot Wi-Fi during matches. Systemcore becomes legal for FTC only from 2027-28.

### Cited Findings
**2026-27 manual R704 (TU04)**
- R704 "Use networks and bandwidth as directed":
  - **D.** "Software may only stream robot control data, debugging data, and telemetry to and from the ROBOT using the FTC Driver Station Application. Additional logging/streaming services, such as those hosted by third party plugins and tools such as FTC Dashboard, FTControl Panels, and others are prohibited. No continuous video stream is allowed."
  - **C.** "Ensure all programming laptops and other devices (other than the DRIVER STATION device) are disconnected from the ROBOT CONTROLLER Wi-Fi network during MATCH play."
  - **B.** "No other devices may attempt to connect to … the ROBOT CONTROLLER Wi-Fi network."
- Source: [2026-2027 Competition Manual TU04, Section 12, p.140](https://ftc-resources.firstinspires.org/ftc/game/manual)
- The same manual's field calibration and measurement time allows "ROBOT may be connected to programming laptops and other devices". — [2026-2027 Competition Manual TU04](https://ftc-resources.firstinspires.org/ftc/game/manual)
- R711 requires a non-default Control Hub Wi-Fi password and Bluetooth disabled. — [2026-2027 Competition Manual TU04](https://ftc-resources.firstinspires.org/ftc/game/manual)

**Comparison with 2025-26 (historical)**
- The DECODE manual (Team Update 32) rule R706 said only: "Software with access to the ROBOT network must limit the amount of data being streamed … Software may only stream robot control data, debugging data, and telemetry to and from the ROBOT. No continuous video stream is allowed." It named no third-party tools. — [2025-2026 Competition Manual TU32 (archive)](https://ftc-resources.firstinspires.org/ftc/archive/2026/game/manual)

**Present since V1**
- A kickoff summary of the V1 manual, dated 2026-09-12, already lists FTC Dashboard and FTControl Panels as prohibited under R704. — [firsthub.dev BIOBUZZ kickoff summary](https://www.firsthub.dev/blog/biobuzz-kickoff-ftc-2026-2027)

**Systemcore timeline**
- FIRST's control-system update (2025-12-08): Systemcore is legal for FTC "starting with the 2027-28 season", with the legacy and hybrid systems legal through at least 2030-31. The Driver Hub replacement is delayed, and alpha teams use a WPILib-developed desktop driver app. — [FIRST blog: Control System Update – FTC Edition](https://community.firstinspires.org/control-system-update-first-tech-challenge-edition)

**SDK 12.0**
- No changes to Wi-Fi or ports relevant to dashboards were found in the release notes. — [FtcRobotController releases](https://github.com/FIRST-Tech-Challenge/FtcRobotController/releases)

### Inferences
- R704.D applies equally to Panels and FTC Dashboard, so it does not favour either tool. It does mean that at 2026-27 events, teams should make sure the dashboard server is disabled or not streaming, and that no laptop is connected during matches.
- R704.D may increase interest in on-robot logging that is reviewed after the match and not streamed live, such as FateWeaver logs or SDK logs. That reading is untested: the rule text targets "logging/streaming services", so logging tools could also be affected.
- Dashboards remain fully usable in a team's own workshop practice, where competition rules do not apply.
- Whether simply having the library installed (but idle) is legal is not stated explicitly.
- The 2027-28 Systemcore transition is likely to shift the dashboard landscape toward the WPILib-style tools (AdvantageScope, the WPILib driver app). That is speculative for FTC and depends on team adoption during the transition years.

### Gaps
- I found no official Q&A ruling on R704.D's scope: pits versus practice field versus matches, whether an installed-but-disabled dashboard is legal, and whether on-robot logging such as FateWeaver counts as a prohibited "logging service". The Q&A forum (opened 2026-09-28 per the manual) needs a team account to search.
- I did not determine whether R704.D's wording changed between V1 and TU04; the manual has no revision log.
- I found no FIRST statement explaining why third-party dashboards were named this season.
