#!/usr/bin/env bash

set -euo pipefail

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
repo_root="$(cd "${script_dir}/.." && pwd)"

java_major_version() {
  local candidate="$1"
  local version_line

  [[ -x "${candidate}/bin/java" ]] || return 1
  version_line="$("${candidate}/bin/java" -version 2>&1 | sed -n '1p')" || return 1
  [[ "${version_line}" =~ \"([0-9]+) ]] || return 1
  printf '%s\n' "${BASH_REMATCH[1]}"
}

is_java_21() {
  local major
  major="$(java_major_version "$1")" || return 1
  [[ "${major}" == "21" ]]
}

selected_java_home=""
if [[ -n "${JAVA_HOME:-}" ]] && is_java_21 "${JAVA_HOME}"; then
  selected_java_home="${JAVA_HOME}"
elif [[ -x /usr/libexec/java_home ]]; then
  mac_java_home="$(/usr/libexec/java_home -v 21 2>/dev/null || true)"
  if [[ -n "${mac_java_home}" ]] && is_java_21 "${mac_java_home}"; then
    selected_java_home="${mac_java_home}"
  fi
fi

if [[ -z "${selected_java_home}" ]]; then
  printf '%s\n' 'No usable JDK 21 was found for this local build.' >&2
  if [[ -n "${JAVA_HOME:-}" ]]; then
    detected_major="$(java_major_version "${JAVA_HOME}" 2>/dev/null || true)"
    if [[ -n "${detected_major}" ]]; then
      printf 'JAVA_HOME points to Java %s: %s\n' "${detected_major}" "${JAVA_HOME}" >&2
    else
      printf 'JAVA_HOME does not contain an executable bin/java: %s\n' "${JAVA_HOME}" >&2
    fi
  fi
  printf '%s\n' \
    'Install or select JDK 21, set JAVA_HOME to that JDK, and set the Gradle JDK in Android Studio to Java 21.' \
    'On macOS, /usr/libexec/java_home -v 21 can locate an installed JDK.' >&2
  exit 2
fi

export JAVA_HOME="${selected_java_home}"
export PATH="${JAVA_HOME}/bin:${PATH}"

if (($# == 0)); then
  set -- :TeamCode:assembleDebug --console=plain
fi

for argument in "$@"; do
  [[ "${argument}" == -* ]] && continue
  task_name="${argument##*:}"
  case "${task_name}" in
    install*|uninstall*|deploy*|connected*|device*|flash*|push*)
      printf 'Refusing device install/deploy task "%s". Use this helper for local builds only.\n' "${argument}" >&2
      exit 2
      ;;
  esac
done

cd "${repo_root}"
printf 'Using Java 21 from %s\n' "${JAVA_HOME}"
exec "${repo_root}/gradlew" "$@"
