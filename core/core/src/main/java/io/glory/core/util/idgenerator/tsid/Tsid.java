package io.glory.core.util.idgenerator.tsid;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Arrays;
import java.util.SplittableRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <a href="https://github.com/f4b6a3/tsid-creator">TSID </a>
 * <p>
 * A value object that represents a Time-Sorted Unique Identifier (TSID).
 * <p>
 * TSID is a 64-bit value that has 2 components:
 * <ul>
 * <li><b>Time component (42 bits)</b>: a number of milliseconds since
 * 2020-01-01T00:00:00.000Z.
 * <li><b>Random component (22 bits)</b>: a sequence of random bits generated by
 * a secure random generator.
 * </ul>
 * <p>
 * The Random component has 2 sub-parts:
 * <ul>
 * <li><b>Node (0 to 20 bits)</b>: a number used to identify the machine or
 * node.
 * <li><b>Counter (2 to 22 bits)</b>: a randomly generated number that is
 * incremented whenever the time component is repeated.
 * </ul>
 * <p>
 * The random component layout depend on the node bits. If the node bits are 10,
 * the counter bits are limited to 12. In this example, the maximum node value
 * is 2^10-1 = 1023 and the maximum counter value is 2^12-1 = 4093. So the
 * maximum TSIDs that can be generated per millisecond per node is 4096.
 * <p>
 * Instances of this class are <b>immutable</b>.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Snowflake_ID">Snowflake ID</a>
 */
public final class Tsid implements Serializable, Comparable<Tsid> {

    /**
     * Number of bytes of a TSID.
     */
    public static final  int    TSID_BYTES         = 8;
    /**
     * Number of characters of a TSID.
     */
    public static final  int    TSID_CHARS         = 13;
    /**
     * Number of milliseconds of 2020-01-01T00:00:00.000Z.
     */
    public static final  long   TSID_EPOCH         = Instant.parse("2020-01-01T00:00:00.000Z").toEpochMilli();
    static final         int    RANDOM_BITS        = 22;
    static final         int    RANDOM_MASK        = 0x003fffff;
    @Serial
    private static final long   serialVersionUID   = -5446820982139116297L;
    private static final char[] ALPHABET_UPPERCASE = "0123456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray();
    private static final char[] ALPHABET_LOWERCASE = "0123456789abcdefghjkmnpqrstvwxyz".toCharArray();
    private static final long[] ALPHABET_VALUES    = new long[128];

    static {
        Arrays.fill(ALPHABET_VALUES, -1);
        // Numbers
        ALPHABET_VALUES['0'] = 0x00;
        ALPHABET_VALUES['1'] = 0x01;
        ALPHABET_VALUES['2'] = 0x02;
        ALPHABET_VALUES['3'] = 0x03;
        ALPHABET_VALUES['4'] = 0x04;
        ALPHABET_VALUES['5'] = 0x05;
        ALPHABET_VALUES['6'] = 0x06;
        ALPHABET_VALUES['7'] = 0x07;
        ALPHABET_VALUES['8'] = 0x08;
        ALPHABET_VALUES['9'] = 0x09;
        // Lower case
        ALPHABET_VALUES['a'] = 0x0a;
        ALPHABET_VALUES['b'] = 0x0b;
        ALPHABET_VALUES['c'] = 0x0c;
        ALPHABET_VALUES['d'] = 0x0d;
        ALPHABET_VALUES['e'] = 0x0e;
        ALPHABET_VALUES['f'] = 0x0f;
        ALPHABET_VALUES['g'] = 0x10;
        ALPHABET_VALUES['h'] = 0x11;
        ALPHABET_VALUES['j'] = 0x12;
        ALPHABET_VALUES['k'] = 0x13;
        ALPHABET_VALUES['m'] = 0x14;
        ALPHABET_VALUES['n'] = 0x15;
        ALPHABET_VALUES['p'] = 0x16;
        ALPHABET_VALUES['q'] = 0x17;
        ALPHABET_VALUES['r'] = 0x18;
        ALPHABET_VALUES['s'] = 0x19;
        ALPHABET_VALUES['t'] = 0x1a;
        ALPHABET_VALUES['v'] = 0x1b;
        ALPHABET_VALUES['w'] = 0x1c;
        ALPHABET_VALUES['x'] = 0x1d;
        ALPHABET_VALUES['y'] = 0x1e;
        ALPHABET_VALUES['z'] = 0x1f;
        // Lower case OIL
        ALPHABET_VALUES['o'] = 0x00;
        ALPHABET_VALUES['i'] = 0x01;
        ALPHABET_VALUES['l'] = 0x01;
        // Upper case
        ALPHABET_VALUES['A'] = 0x0a;
        ALPHABET_VALUES['B'] = 0x0b;
        ALPHABET_VALUES['C'] = 0x0c;
        ALPHABET_VALUES['D'] = 0x0d;
        ALPHABET_VALUES['E'] = 0x0e;
        ALPHABET_VALUES['F'] = 0x0f;
        ALPHABET_VALUES['G'] = 0x10;
        ALPHABET_VALUES['H'] = 0x11;
        ALPHABET_VALUES['J'] = 0x12;
        ALPHABET_VALUES['K'] = 0x13;
        ALPHABET_VALUES['M'] = 0x14;
        ALPHABET_VALUES['N'] = 0x15;
        ALPHABET_VALUES['P'] = 0x16;
        ALPHABET_VALUES['Q'] = 0x17;
        ALPHABET_VALUES['R'] = 0x18;
        ALPHABET_VALUES['S'] = 0x19;
        ALPHABET_VALUES['T'] = 0x1a;
        ALPHABET_VALUES['V'] = 0x1b;
        ALPHABET_VALUES['W'] = 0x1c;
        ALPHABET_VALUES['X'] = 0x1d;
        ALPHABET_VALUES['Y'] = 0x1e;
        ALPHABET_VALUES['Z'] = 0x1f;
        // Upper case OIL
        ALPHABET_VALUES['O'] = 0x00;
        ALPHABET_VALUES['I'] = 0x01;
        ALPHABET_VALUES['L'] = 0x01;
    }

    private final long number;

    /**
     * Creates a new TSID.
     * <p>
     * This constructor wraps the input value in an immutable object.
     *
     * @param number a number
     */
    public Tsid(final long number) {
        this.number = number;
    }

    /**
     * Converts a number into a TSID.
     * <p>
     * This method wraps the input value in an immutable object.
     *
     * @param number a number
     * @return a TSID
     */
    public static Tsid from(final long number) {
        return new Tsid(number);
    }

    /**
     * Converts a byte array into a TSID.
     *
     * @param bytes a byte array
     * @return a TSID
     * @throws IllegalArgumentException if bytes are null or its length is not 8
     */
    public static Tsid from(final byte[] bytes) {

        if (bytes == null || bytes.length != TSID_BYTES) {
            throw new IllegalArgumentException("Invalid TSID bytes"); // null or wrong length!
        }

        long number = 0;

        number |= (bytes[0x0] & 0xffL) << 56;
        number |= (bytes[0x1] & 0xffL) << 48;
        number |= (bytes[0x2] & 0xffL) << 40;
        number |= (bytes[0x3] & 0xffL) << 32;
        number |= (bytes[0x4] & 0xffL) << 24;
        number |= (bytes[0x5] & 0xffL) << 16;
        number |= (bytes[0x6] & 0xffL) << 8;
        number |= (bytes[0x7] & 0xffL);

        return new Tsid(number);
    }

    /**
     * Converts a canonical string into a TSID.
     * <p>
     * The input string must be 13 characters long and must contain only characters
     * from Crockford's base 32 alphabet.
     * <p>
     * The first character of the input string must be between 0 and F.
     *
     * @param string a canonical string
     * @return a TSID
     * @throws IllegalArgumentException if the input string is invalid
     * @see <a href="https://www.crockford.com/base32.html">Crockford's Base 32</a>
     */
    public static Tsid from(final String string) {

        final char[] chars = toCharArray(string);

        long number = 0;

        number |= ALPHABET_VALUES[chars[0x00]] << 60;
        number |= ALPHABET_VALUES[chars[0x01]] << 55;
        number |= ALPHABET_VALUES[chars[0x02]] << 50;
        number |= ALPHABET_VALUES[chars[0x03]] << 45;
        number |= ALPHABET_VALUES[chars[0x04]] << 40;
        number |= ALPHABET_VALUES[chars[0x05]] << 35;
        number |= ALPHABET_VALUES[chars[0x06]] << 30;
        number |= ALPHABET_VALUES[chars[0x07]] << 25;
        number |= ALPHABET_VALUES[chars[0x08]] << 20;
        number |= ALPHABET_VALUES[chars[0x09]] << 15;
        number |= ALPHABET_VALUES[chars[0x0a]] << 10;
        number |= ALPHABET_VALUES[chars[0x0b]] << 5;
        number |= ALPHABET_VALUES[chars[0x0c]];

        return new Tsid(number);
    }

    /**
     * Returns a fast new TSID.
     * <p>
     * This static method is a quick alternative to {@link TsidCreator#getTsid()}.
     * <p>
     * It employs {@link AtomicInteger} to generate up to 2^22 (4,194,304) TSIDs per
     * millisecond. It can be useful, for example, for logging.
     * <p>
     * Security-sensitive applications that require a cryptographically secure
     * pseudo-random generator <b>should</b> use {@link TsidCreator#getTsid()}.
     * <p>
     * System property "tsidcreator.node" and environment variable
     * "TSIDCREATOR_NODE" are ignored by this method. Therefore, there will be
     * collisions if more than one process is generating TSIDs using this method. In
     * that case, {@link TsidCreator#getTsid()} <b>should</b> be used in conjunction
     * with that property or variable.
     *
     * @return a TSID
     * @see AtomicInteger
     * @since 5.1.0
     */
    public static Tsid fast() {
        final long time = (System.currentTimeMillis() - TSID_EPOCH) << RANDOM_BITS;
        final long tail = LazyHolder.counter.incrementAndGet() & RANDOM_MASK;
        return new Tsid(time | tail);
    }

    /**
     * Checks if the input string is valid.
     * <p>
     * The input string must be 13 characters long and must contain only characters
     * from Crockford's base 32 alphabet.
     * <p>
     * The first character of the input string must be between 0 and F.
     *
     * @param string a string
     * @return true if valid
     */
    public static boolean isValid(final String string) {
        return string != null && isValidCharArray(string.toCharArray());
    }

    /**
     * Converts a base-n encoded string to a TSID.
     * <p>
     * Example:
     * <ul>
     * <li>String: 05772439BB9F9074
     * <li>Base: 16
     * <li>Output: 0AXS476XSZ43M
     * </ul>
     * <p>
     * The input string must be left padded with zeros.
     * <p>
     * <b>Note</b>: this method is CASE-SENSITIVE.
     *
     * @param string a base-n encoded string
     * @param base   a radix between 2 and 62
     * @return a TSID
     * @throws IllegalArgumentException if the string or base is invalid
     * @since 5.2.0
     */
    public static Tsid decode(final String string, final int base) {
        return BaseN.decode(string, base);
    }

    /**
     * Converts a string using a custom format to a TSID.
     * <p>
     * This method does the opposite operation of {@link Tsid#format(String)}.
     * <p>
     * Examples:
     * <ul>
     * <li>An key that starts with a letter:
     * <ul>
     * <li>String: K<b>0AWE5HZP3SKTK</b>
     * <li>Format: K%S
     * <li>Output: 0AWE5HZP3SKTK
     * </ul>
     * </li>
     * <li>A file name in hexadecimal with a prefix and an extension:
     * <ul>
     * <li>String: DOC-<b>0575FDC1786137D6</b>.PDF
     * <li>Format: DOC-%X.PDF
     * <li>Output: 0AXFXR5W7VBX0
     * </ul>
     * </li>
     * </ul>
     *
     * @param formatted a string using a custom format
     * @param format    a custom format
     * @return a TSID
     * @throws IllegalArgumentException if the formatted string or the format string
     *                                  is invalid
     * @since 5.2.0
     */
    public static Tsid unformat(final String formatted, final String format) {
        if (formatted != null && format != null) {
            final int i = format.indexOf("%");
            if (i < 0 || i == format.length() - 1) {
                throw new IllegalArgumentException(String.format("Invalid format string: \"%s\"", format));
            }
            final String head = format.substring(0, i);
            final String tail = format.substring(i + 2);
            final char placeholder = format.charAt(i + 1);
            final int length = formatted.length() - head.length() - tail.length();
            if (formatted.startsWith(head) && formatted.endsWith(tail)) {
                return switch (placeholder) {
                    case 'S' -> // canonical string (case insensitive)
                            Tsid.from(formatted.substring(i, i + length));
                    case 's' -> // canonical string (case insensitive)
                            Tsid.from(formatted.substring(i, i + length));
                    case 'X' -> // hexadecimal (case insensitive)
                            BaseN.decode(formatted.substring(i, i + length).toUpperCase(), 16);
                    case 'x' -> // hexadecimal (case insensitive)
                            BaseN.decode(formatted.substring(i, i + length).toUpperCase(), 16);
                    case 'd' -> // base-10
                            BaseN.decode(formatted.substring(i, i + length), 10);
                    case 'z' -> // base-62
                            BaseN.decode(formatted.substring(i, i + length), 62);
                    default -> throw new IllegalArgumentException(
                            String.format("Invalid placeholder: \"%%%s\"", placeholder));
                };
            }
        }
        throw new IllegalArgumentException(String.format("Invalid formatted string: \"%s\"", formatted));
    }

    /**
     * Converts the TSID into a number.
     * <p>
     * This method simply unwraps the internal value.
     *
     * @return an number.
     */
    public long toLong() {
        return this.number;
    }

    /**
     * Converts the TSID into a byte array.
     *
     * @return an byte array.
     */
    public byte[] toBytes() {

        final byte[] bytes = new byte[TSID_BYTES];

        bytes[0x0] = (byte)(number >>> 56);
        bytes[0x1] = (byte)(number >>> 48);
        bytes[0x2] = (byte)(number >>> 40);
        bytes[0x3] = (byte)(number >>> 32);
        bytes[0x4] = (byte)(number >>> 24);
        bytes[0x5] = (byte)(number >>> 16);
        bytes[0x6] = (byte)(number >>> 8);
        bytes[0x7] = (byte)(number);

        return bytes;
    }

    /**
     * Converts the TSID into a canonical string in upper case.
     * <p>
     * The output string is 13 characters long and contains only characters from
     * Crockford's base 32 alphabet.
     * <p>
     * For lower case string, use the shorthand {@code Tsid.toLowerCase()} instead
     * of {@code Tsid.toString().toLowerCase()}.
     *
     * @return a TSID string
     * @see <a href="https://www.crockford.com/base32.html">Crockford's Base 32</a>
     */
    @Override
    public String toString() {
        return toString(ALPHABET_UPPERCASE);
    }

    /**
     * Converts the TSID into a canonical string in lower case.
     * <p>
     * The output string is 13 characters long and contains only characters from
     * Crockford's base 32 alphabet.
     * <p>
     * It is faster shorthand for {@code Tsid.toString().toLowerCase()}.
     *
     * @return a string
     * @see <a href="https://www.crockford.com/base32.html">Crockford's Base 32</a>
     */
    public String toLowerCase() {
        return toString(ALPHABET_LOWERCASE);
    }

    /**
     * Returns the instant of creation.
     * <p>
     * The instant of creation is extracted from the time component.
     *
     * @return {@link Instant}
     */
    public Instant getInstant() {
        return Instant.ofEpochMilli(getUnixMilliseconds());
    }

    /**
     * Returns the instant of creation.
     * <p>
     * The instant of creation is extracted from the time component.
     *
     * @param customEpoch the custom epoch instant
     * @return {@link Instant}
     */
    public Instant getInstant(final Instant customEpoch) {
        return Instant.ofEpochMilli(getUnixMilliseconds(customEpoch.toEpochMilli()));
    }

    /**
     * Returns the time of creation in milliseconds since 1970-01-01.
     * <p>
     * The time of creation is extracted from the time component.
     *
     * @return the number of milliseconds since 1970-01-01
     */
    public long getUnixMilliseconds() {
        return this.getTime() + TSID_EPOCH;
    }

    /**
     * Returns the time of creation in milliseconds since 1970-01-01.
     * <p>
     * The time of creation is extracted from the time component.
     *
     * @param customEpoch the custom epoch in milliseconds since 1970-01-01
     * @return the number of milliseconds since 1970-01-01
     */
    public long getUnixMilliseconds(final long customEpoch) {
        return this.getTime() + customEpoch;
    }

    /**
     * Returns a hash code value for the TSID.
     */
    @Override
    public int hashCode() {
        return Long.hashCode(number);
    }

    /**
     * Checks if some other TSID is equal to this one.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (other.getClass() != Tsid.class)
            return false;
        Tsid that = (Tsid)other;
        return (this.number == that.number);
    }

    /**
     * Compares two TSIDs as <b>unsigned</b> 64-bit integers.
     * <p>
     * The first of two TSIDs is greater than the second if the most significant
     * byte in which they differ is greater for the first TSID.
     *
     * @param other a TSID to be compared with
     * @return -1, 0 or 1 as {@code this} is less than, equal to, or greater than
     * {@code other}
     */
    @Override
    public int compareTo(Tsid other) {

        // used to compare as UNSIGNED longs
        final long min = 0x8000000000000000L;

        final long a = this.number + min;
        final long b = other.number + min;

        if (a > b)
            return 1;
        else if (a < b)
            return -1;

        return 0;
    }

    /**
     * Converts the TSID to a base-n encoded string.
     * <p>
     * Example:
     * <ul>
     * <li>TSID: 0AXS751X00W7C
     * <li>Base: 62
     * <li>Output: 0T5jFDIkmmy
     * </ul>
     * <p>
     * The output string is left padded with zeros.
     *
     * @param base a radix between 2 and 62
     * @return a base-n encoded string
     * @throws IllegalArgumentException if the base is invalid
     * @since 5.2.0
     */
    public String encode(final int base) {
        return BaseN.encode(this, base);
    }

    /**
     * Converts the TSID to a string using a custom format.
     * <p>
     * The custom format uses a placeholder that will be substituted by the TSID
     * string. Only the first occurrence of a placeholder will replaced.
     * <p>
     * Placeholders:
     * <ul>
     * <li>%S: canonical string in upper case
     * <li>%s: canonical string in lower case
     * <li>%X: hexadecimal in upper case
     * <li>%x: hexadecimal in lower case
     * <li>%d: base-10
     * <li>%z: base-62
     * </ul>
     * <p>
     * Examples:
     * <ul>
     * <li>An key that starts with a letter:
     * <ul>
     * <li>TSID: 0AWE5HZP3SKTK
     * <li>Format: K%S
     * <li>Output: K<b>0AWE5HZP3SKTK</b>
     * </ul>
     * </li>
     * <li>A file name in hexadecimal with a prefix and an extension:
     * <ul>
     * <li>TSID: 0AXFXR5W7VBX0
     * <li>Format: DOC-%X.PDF
     * <li>Output: DOC-<b>0575FDC1786137D6</b>.PDF
     * </ul>
     * </li>
     * </ul>
     * <p>
     * The opposite operation can be done by {@link Tsid#unformat(String, String)}.
     *
     * @param format a custom format
     * @return a string using a custom format
     * @throws IllegalArgumentException if the format string is invalid
     * @since 5.2.0
     */
    public String format(final String format) {
        if (format != null) {
            final int i = format.indexOf("%");
            if (i < 0 || i == format.length() - 1) {
                throw new IllegalArgumentException(String.format("Invalid format string: \"%s\"", format));
            }
            final String head = format.substring(0, i);
            final String tail = format.substring(i + 2);
            final char placeholder = format.charAt(i + 1);
            return switch (placeholder) {
                case 'S' -> // canonical string in upper case
                        head + toString() + tail;
                case 's' -> // canonical string in lower case
                        head + toLowerCase() + tail;
                case 'X' -> // hexadecimal in upper case
                        head + BaseN.encode(this, 16) + tail;
                case 'x' -> // hexadecimal in lower case
                        head + BaseN.encode(this, 16).toLowerCase() + tail;
                case 'd' -> // base-10
                        head + BaseN.encode(this, 10) + tail;
                case 'z' -> // base-62
                        head + BaseN.encode(this, 62) + tail;
                default ->
                        throw new IllegalArgumentException(String.format("Invalid placeholder: \"%%%s\"", placeholder));
            };
        }
        throw new IllegalArgumentException(String.format("Invalid format string: \"%s\"", format));
    }

    /**
     * Returns the time component as a number.
     * <p>
     * The time component is a number between 0 and 2^42-1.
     *
     * @return a number of milliseconds.
     */
    long getTime() {
        return this.number >>> RANDOM_BITS;
    }

    /**
     * Returns the random component as a number.
     * <p>
     * The time component is a number between 0 and 2^22-1.
     *
     * @return a number
     */
    long getRandom() {
        return this.number & RANDOM_MASK;
    }

    String toString(final char[] alphabet) {

        final char[] chars = new char[TSID_CHARS];

        chars[0x00] = alphabet[(int)((number >>> 60) & 0b11111)];
        chars[0x01] = alphabet[(int)((number >>> 55) & 0b11111)];
        chars[0x02] = alphabet[(int)((number >>> 50) & 0b11111)];
        chars[0x03] = alphabet[(int)((number >>> 45) & 0b11111)];
        chars[0x04] = alphabet[(int)((number >>> 40) & 0b11111)];
        chars[0x05] = alphabet[(int)((number >>> 35) & 0b11111)];
        chars[0x06] = alphabet[(int)((number >>> 30) & 0b11111)];
        chars[0x07] = alphabet[(int)((number >>> 25) & 0b11111)];
        chars[0x08] = alphabet[(int)((number >>> 20) & 0b11111)];
        chars[0x09] = alphabet[(int)((number >>> 15) & 0b11111)];
        chars[0x0a] = alphabet[(int)((number >>> 10) & 0b11111)];
        chars[0x0b] = alphabet[(int)((number >>> 5) & 0b11111)];
        chars[0x0c] = alphabet[(int)(number & 0b11111)];

        return new String(chars);
    }

    static char[] toCharArray(final String string) {
        char[] chars = string == null ? null : string.toCharArray();
        if (!isValidCharArray(chars)) {
            throw new IllegalArgumentException(String.format("Invalid TSID string: \"%s\"", string));
        }
        return chars;
    }

    /**
     * Checks if the string is a valid TSID.
     * <p>
     * A valid TSID string is a sequence of 13 characters from Crockford's base 32
     * alphabet.
     * <p>
     * The first character of the input string must be between 0 and F.
     *
     * @param chars a char array
     * @return boolean true if valid
     */
    static boolean isValidCharArray(final char[] chars) {

        if (chars == null || chars.length != TSID_CHARS) {
            return false; // null or wrong size!
        }

        // The extra bit added by base-32 encoding must be zero
        // As a consequence, the 1st char of the input string must be between 0 and F.
        if ((ALPHABET_VALUES[chars[0]] & 0b10000) != 0) {
            return false; // overflow!
        }

        for (char aChar : chars) {
            if (ALPHABET_VALUES[aChar] == -1) {
                return false; // invalid character!
            }
        }
        return true; // It seems to be OK.
    }

    static class BaseN {

        static final BigInteger MAX      = BigInteger.valueOf(2).pow(64).subtract(BigInteger.ONE);
        static final String     ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; // base-62

        private static int length(int base) {
            return (int)Math.ceil(Long.SIZE / (Math.log(base) / Math.log(2)));
        }

        private static void exception(String string) {
            throw new IllegalArgumentException(string);
        }

        static String encode(final Tsid tsid, final int base) {

            if (base < 2 || base > 62) {
                exception(String.format("Invalid base: %s", base));
            }

            long x = tsid.number;
            int b = length(base);
            char[] buffer = new char[b];

            while (Long.compareUnsigned(x, 0) > 0) {
                final long div = Long.divideUnsigned(x, base);
                final long rem = Long.remainderUnsigned(x, base);
                buffer[--b] = ALPHABET.charAt((int)rem);
                x = div;
            }

            while (b > 0) {
                buffer[--b] = '0';
            }
            return new String(buffer);
        }

        static Tsid decode(final String string, final int base) {

            if (string == null) {
                exception(String.format("Invalid base-%d string: null", base));
            }
            if (base < 2 || base > 62) {
                exception(String.format("Invalid base: %s", base));
            }

            long x = 0L;
            long last = 0;
            long plus = 0;

            final int length = length(base);
            if (string.length() != length) {
                exception(String.format("Invalid base-%d length: %s", base, string.length()));
            }

            for (int i = 0; i < length; i++) {

                plus = ALPHABET.indexOf(string.charAt(i));
                if (plus == -1) {
                    exception(String.format("Invalid base-%d character: %s", base, string.charAt(i)));
                }

                last = x;
                x = (x * base) + plus;
            }

            // finally, check if happened an overflow
            ByteBuffer buff = ByteBuffer.allocate(8);
            byte[] bytes = buff.putLong(last).array();
            BigInteger lazt = new BigInteger(1, bytes);
            BigInteger baze = BigInteger.valueOf(base);
            BigInteger pluz = BigInteger.valueOf(plus);
            if (lazt.multiply(baze).add(pluz).compareTo(MAX) > 0) {
                throw new IllegalArgumentException(String.format("Invalid base-%d value (overflow): %s", base, lazt));
            }

            return new Tsid(x);
        }

    }

    private static class LazyHolder {

        private static final AtomicInteger counter = new AtomicInteger((new SplittableRandom()).nextInt());

    }

}
