#!/usr/bin/env sh
# Copyright 2024 Code Intelligence GmbH
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

set -eu


bazel build //:jazzer
bazel build //deploy:jazzer-docs //deploy:jazzer-sources //deploy:jazzer-pom //deploy:jazzer-api-docs

bazel run --define "maven_repo=file://$HOME/.m2/repository" //deploy:jazzer.publish
bazel run --define "maven_repo=file://$HOME/.m2/repository" //deploy:jazzer-api.publish
bazel run --define "maven_repo=file://$HOME/.m2/repository" //deploy:jazzer-junit.publish
