/*
 * Copyright 2024 Code Intelligence GmbH
 *
 * By downloading, you agree to the Code Intelligence Jazzer Terms and Conditions.
 *
 * The Code Intelligence Jazzer Terms and Conditions are provided in LICENSE-JAZZER.txt
 * located in the root directory of the project.
 */

package com.code_intelligence.jazzer.instrumentor;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

public final class UnsafeCoverageMap {
  private static final Unsafe UNSAFE;

  static {
    Unsafe unsafe;
    try {
      Field f = Unsafe.class.getDeclaredField("theUnsafe");
      f.setAccessible(true);
      unsafe = (Unsafe) f.get(null);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      e.printStackTrace();
      System.exit(1);
      // Not reached.
      unsafe = null;
    }
    UNSAFE = unsafe;
  }

  // The current target, JsonSanitizer, uses less than 2048 coverage counters.
  private static final long NUM_COUNTERS = 4096;
  private static final long countersAddress = UNSAFE.allocateMemory(NUM_COUNTERS);

  static {
    UNSAFE.setMemory(countersAddress, NUM_COUNTERS, (byte) 0);
  }

  public static void enlargeIfNeeded(int nextId) {
    // Statically sized counters buffer.
  }

  public static void recordCoverage(final int id) {
    final long address = countersAddress + id;
    final byte counter = UNSAFE.getByte(address);
    if (counter == -1) {
      UNSAFE.putByte(address, (byte) 1);
    } else {
      UNSAFE.putByte(address, (byte) (counter + 1));
    }
  }
}
