package com.wurmcraft.serveressentials.common.utils;

import com.sun.corba.se.impl.io.TypeMismatchException;
import java.lang.reflect.Array;

public class ArrayUtils {

  public static <T> T[] copyOver(T[] original, T[] receiver) {
    return ArrayUtils.<T>copyOver(original, 0, original.length, receiver);
  }

  public static <T> T[] copyOver(T[] original, int off, int len, T[] receiver)
      throws IndexOutOfBoundsException {
    if (len >= 0) {
      for (int pos = off; pos < off + len && pos < original.length; pos++) {
        receiver[pos - off] = original[pos];
      }
    } else {
      for (int pos = off; pos >= (-1 * len) && pos >= 0; pos--) {
        receiver[off - pos] = original[pos];
      }
    }
    return receiver;
  }

  public static <T> T[] splice(T[] array, int start, int end) {
    Range<Integer> newSize = new Range<Integer>(start, end);
    Class<T> type = (Class<T>) array.getClass().getComponentType();
    T[] workingArray = (T[]) Array.newInstance(type, Math.abs(newSize.difference()) + 1);
    return (start > end)
        ? copyOver(array, start, -1 * end, workingArray)
        : copyOver(array, start, end + 1, workingArray);
  }

  public static final class Range<T extends Number> {

    public final T lower;

    public final T upper;

    public Range(T lower, T upper) {
      this.lower = lower;
      this.upper = upper;
    }

    public static <T extends Number> boolean areEqual(Range<T> first, Range<T> second) {
      return first.getUpperLimit() == second.getUpperLimit()
          && first.getLowerLimit() == second.getLowerLimit();
    }

    public static <T extends Number> Range<T> getDifference(Range<T> first, Range<T> second) {
      return new Range<T>(
          (T) new Integer(Math.abs(first.lower.byteValue() - second.lower.byteValue())),
          (T) new Integer(Math.abs(first.upper.byteValue() - second.upper.byteValue())));
    }

    public T getLowerLimit() {
      return this.lower;
    }

    public T getUpperLimit() {
      return this.upper;
    }

    @Override
    public String toString() {
      return (this.lower + " : " + this.upper);
    }

    @Override
    public boolean equals(Object o) throws TypeMismatchException {
      Range<T> range = null;
      try {
        range = (Range<T>) o;
      } catch (ClassCastException e) {
        throw new TypeMismatchException();
      }
      return Range.areEqual(this, range);
    }

    public boolean isWithinRange(T arbitraryNumber) {
      return (arbitraryNumber.doubleValue() >= this.lower.doubleValue()
          && arbitraryNumber.doubleValue() <= this.upper.doubleValue());
    }

    public T difference() {
      switch (this.lower.getClass().getSimpleName()) {
        case "Long":
          {
            return (T) new Long(this.upper.longValue() - this.lower.longValue());
          }
        case "Float":
          {
            return (T) new Float(this.upper.floatValue() - this.lower.floatValue());
          }
        case "Integer":
          {
            return (T) new Integer(this.upper.intValue() - this.lower.intValue());
          }
        default:
          return (T) new Double(this.upper.doubleValue() - this.lower.doubleValue());
      }
    }

    public boolean isGreaterThan(Range<T> supposedlyLessor) {
      return this.difference().doubleValue() > supposedlyLessor.difference().doubleValue();
    }

    public boolean isLessThan(Range<T> supposedlyGreater) {
      return !isGreaterThan(supposedlyGreater);
    }
  }
}
