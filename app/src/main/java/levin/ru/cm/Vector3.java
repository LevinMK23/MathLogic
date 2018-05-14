package levin.ru.cm;

public final class Vector3 {
    public static final Vector3 ZERO = new Vector3(0.0f, 0.0f, 0.0f);
    public float f5x;
    public float f6y;
    public float f7z;

    public Vector3() {
        set(0.0f, 0.0f, 0.0f);
    }

    public Vector3(float xValue, float yValue, float zValue) {
        set(xValue, yValue, zValue);
    }

    public Vector3(float xValue, float yValue) {
        set(xValue, yValue);
    }

    public Vector3(Vector3 other) {
        set(other);
    }

    public final void add(Vector3 other) {
        this.f5x += other.f5x;
        this.f6y += other.f6y;
        this.f7z += other.f7z;
    }

    public final void add(float otherX, float otherY, float otherZ) {
        this.f5x += otherX;
        this.f6y += otherY;
        this.f7z += otherZ;
    }

    public final void subtract(Vector3 other) {
        this.f5x -= other.f5x;
        this.f6y -= other.f6y;
        this.f7z -= other.f7z;
    }

    public final void multiply(float magnitude) {
        this.f5x *= magnitude;
        this.f6y *= magnitude;
        this.f7z *= magnitude;
    }

    public final void multiply(Vector3 other) {
        this.f5x *= other.f5x;
        this.f6y *= other.f6y;
        this.f7z *= other.f7z;
    }

    public final void divide(float magnitude) {
        if (magnitude != 0.0f) {
            this.f5x /= magnitude;
            this.f6y /= magnitude;
            this.f7z /= magnitude;
        }
    }

    public final void set(Vector3 other) {
        this.f5x = other.f5x;
        this.f6y = other.f6y;
        this.f7z = other.f7z;
    }

    public final void set(float xValue, float yValue, float zValue) {
        this.f5x = xValue;
        this.f6y = yValue;
        this.f7z = zValue;
    }

    public final void set(float xValue, float yValue) {
        this.f5x = xValue;
        this.f6y = yValue;
        this.f7z = 0.0f;
    }

    public final float dot(Vector3 other) {
        return ((this.f5x * other.f5x) + (this.f6y * other.f6y)) + (this.f7z * other.f7z);
    }

    public final float length() {
        return (float) Math.sqrt((double) length2());
    }

    public final float length2() {
        return ((this.f5x * this.f5x) + (this.f6y * this.f6y)) + (this.f7z * this.f7z);
    }

    public final float distance2(Vector3 other) {
        float dx = this.f5x - other.f5x;
        float dy = this.f6y - other.f6y;
        float dz = this.f7z - other.f7z;
        return ((dx * dx) + (dy * dy)) + (dz * dz);
    }

    static final float distance(Vector3 v1, Vector3 other) {
        float dx = v1.f5x - other.f5x;
        float dy = v1.f6y - other.f6y;
        float dz = v1.f7z - other.f7z;
        return (float) Math.sqrt((double) (((dx * dx) + (dy * dy)) + (dz * dz)));
    }

    static final float distance(float x0, float y0, float x1, float y1) {
        float dx = x0 - x1;
        float dy = y0 - y1;
        return (float) Math.sqrt((double) ((dx * dx) + (dy * dy)));
    }

    public final float normalize() {
        float magnitude = length();
        if (magnitude != 0.0f) {
            this.f5x /= magnitude;
            this.f6y /= magnitude;
            this.f7z /= magnitude;
        }
        return magnitude;
    }

    public final void zero() {
        set(0.0f, 0.0f, 0.0f);
    }
}
