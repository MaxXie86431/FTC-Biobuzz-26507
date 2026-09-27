#!/usr/bin/env bash

set -euo pipefail

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
repo_root="$(cd "${script_dir}/.." && pwd)"

java_major_version() {
  local candidate="$1"
  local version_line

  [[ -x "${candidate}/bin/java" && -x "${candidate}/bin/javac" ]] || return 1
  version_line="$("${candidate}/bin/java" -version 2>&1 | sed -n '1p')" || return 1
  [[ "${version_line}" =~ \"([0-9]+) ]] || return 1
  printf '%s\n' "${BASH_REMATCH[1]}"
}

selected_java_home=""
if [[ -n "${JAVA_HOME:-}" ]] \
    && [[ "$(java_major_version "${JAVA_HOME}" || true)" == "21" ]]; then
  selected_java_home="${JAVA_HOME}"
elif [[ -x /usr/libexec/java_home ]]; then
  mac_java_home="$(/usr/libexec/java_home -v 21 2>/dev/null || true)"
  if [[ -n "${mac_java_home}" ]] \
      && [[ "$(java_major_version "${mac_java_home}" || true)" == "21" ]]; then
    selected_java_home="${mac_java_home}"
  fi
fi

if [[ -z "${selected_java_home}" ]]; then
  printf '%s\n' 'No usable JDK 21 was found. Set JAVA_HOME to JDK 21 or install it for /usr/libexec/java_home -v 21.' >&2
  exit 2
fi

temporary_dir="$(mktemp -d)"
trap 'rm -rf "${temporary_dir}"' EXIT

helper="${repo_root}/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/robot/FieldCentricDrive.java"
test_source="${repo_root}/TeamCode/src/test/java/org/firstinspires/ftc/teamcode/robot/FieldCentricDriveTest.java"
test_class="org.firstinspires.ftc.teamcode.robot.FieldCentricDriveTest"

"${selected_java_home}/bin/javac" --release 8 -d "${temporary_dir}" "${helper}" "${test_source}"
"${selected_java_home}/bin/java" -cp "${temporary_dir}" "${test_class}"
