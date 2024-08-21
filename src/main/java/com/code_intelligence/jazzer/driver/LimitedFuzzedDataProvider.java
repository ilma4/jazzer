/*
 * Copyright 2024 Code Intelligence GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.code_intelligence.jazzer.driver;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.api.NotEnoughFuzzedData;

public final class LimitedFuzzedDataProvider implements FuzzedDataProvider {

  private final FuzzedDataProvider fuzzedDataProvider;

  public LimitedFuzzedDataProvider(FuzzedDataProvider fuzzedDataProvider) {
    this.fuzzedDataProvider = fuzzedDataProvider;
  }

  private void requireEnoughBytes(int required) {
    int remainingBytes = remainingBytes();
    if (remainingBytes < required) {
      throw new NotEnoughFuzzedData(required, remainingBytes);
    }
  }

  private void requireNotEmpty() {
    requireEnoughBytes(1);
  }

  @Override
  public boolean consumeBoolean() {
    requireEnoughBytes(1);
    return fuzzedDataProvider.consumeBoolean();
  }

  @Override
  public boolean[] consumeBooleans(int maxLength) {
    requireNotEmpty();
    return fuzzedDataProvider.consumeBooleans(maxLength);
  }

  @Override
  public byte consumeByte() {
    requireEnoughBytes(Byte.BYTES);
    return fuzzedDataProvider.consumeByte();
  }

  @Override
  public byte consumeByte(byte min, byte max) {
    requireEnoughBytes(Byte.BYTES);
    return fuzzedDataProvider.consumeByte();
  }

  @Override
  public byte[] consumeBytes(int maxLength) {
    requireNotEmpty();
    return fuzzedDataProvider.consumeBytes(maxLength);
  }

  @Override
  public byte[] consumeRemainingAsBytes() {
    return fuzzedDataProvider.consumeRemainingAsBytes();
  }

  @Override
  public short consumeShort() {
    requireEnoughBytes(Short.BYTES);
    return fuzzedDataProvider.consumeShort();
  }

  @Override
  public short consumeShort(short min, short max) {
    requireEnoughBytes(Short.BYTES);
    return fuzzedDataProvider.consumeShort(min, max);
  }

  @Override
  public short[] consumeShorts(int maxLength) {
    requireNotEmpty();
    return fuzzedDataProvider.consumeShorts(maxLength);
  }

  @Override
  public int consumeInt() {
    requireEnoughBytes(Integer.BYTES);
    return fuzzedDataProvider.consumeInt();
  }

  @Override
  public int consumeInt(int min, int max) {
    requireEnoughBytes(Integer.BYTES);
    return fuzzedDataProvider.consumeInt(min, max);
  }

  @Override
  public int[] consumeInts(int maxLength) {
    requireNotEmpty();
    return fuzzedDataProvider.consumeInts(maxLength);
  }

  @Override
  public long consumeLong() {
    requireEnoughBytes(Long.BYTES);
    return fuzzedDataProvider.consumeLong();
  }

  @Override
  public long consumeLong(long min, long max) {
    requireEnoughBytes(Long.BYTES);
    return fuzzedDataProvider.consumeLong(min, max);
  }

  @Override
  public long[] consumeLongs(int maxLength) {
    requireNotEmpty();
    return fuzzedDataProvider.consumeLongs(maxLength);
  }

  // Float's consuming logic is complicated, so just checking if there are bytes

  @Override
  public float consumeFloat() {
    requireNotEmpty();
    return fuzzedDataProvider.consumeFloat();
  }

  @Override
  public float consumeRegularFloat() {
    requireNotEmpty();
    return fuzzedDataProvider.consumeRegularFloat();
  }

  @Override
  public float consumeRegularFloat(float min, float max) {
    requireNotEmpty();
    return fuzzedDataProvider.consumeRegularFloat(min, max);
  }

  @Override
  public float consumeProbabilityFloat() {
    requireNotEmpty();
    return fuzzedDataProvider.consumeProbabilityFloat();
  }

  @Override
  public double consumeDouble() {
    requireNotEmpty();
    return fuzzedDataProvider.consumeDouble();
  }

  @Override
  public double consumeRegularDouble() {
    requireNotEmpty();
    return fuzzedDataProvider.consumeRegularDouble();
  }

  @Override
  public double consumeRegularDouble(double min, double max) {
    requireNotEmpty();
    return fuzzedDataProvider.consumeRegularDouble(min, max);
  }

  @Override
  public double consumeProbabilityDouble() {
    requireNotEmpty();
    return fuzzedDataProvider.consumeProbabilityDouble();
  }

  @Override
  public char consumeChar() {
    requireEnoughBytes(Character.BYTES);
    return fuzzedDataProvider.consumeChar();
  }

  @Override
  public char consumeChar(char min, char max) {
    requireEnoughBytes(Character.BYTES);
    return fuzzedDataProvider.consumeChar(min, max);
  }

  @Override
  public char consumeCharNoSurrogates() {
    requireEnoughBytes(Character.BYTES);
    return fuzzedDataProvider.consumeCharNoSurrogates();
  }

  @Override
  public String consumeString(int maxLength) {
    requireNotEmpty();
    return fuzzedDataProvider.consumeString(maxLength);
  }

  @Override
  public String consumeRemainingAsString() {
    requireNotEmpty();
    return fuzzedDataProvider.consumeRemainingAsString();
  }

  @Override
  public String consumeAsciiString(int maxLength) {
    requireNotEmpty();
    return fuzzedDataProvider.consumeAsciiString(maxLength);
  }

  @Override
  public String consumeRemainingAsAsciiString() {
    return fuzzedDataProvider.consumeRemainingAsAsciiString();
  }

  @Override
  public int remainingBytes() {
    return fuzzedDataProvider.remainingBytes();
  }
}
