package android.support.v4.view.accessibility;

import android.view.accessibility.*;

class AccessibilityEventCompatIcs
{
    public static void appendRecord(final AccessibilityEvent accessibilityEvent, final Object o) {
        accessibilityEvent.appendRecord((AccessibilityRecord)o);
    }
    
    public static Object getRecord(final AccessibilityEvent accessibilityEvent, final int n) {
        return accessibilityEvent.getRecord(n);
    }
    
    public static int getRecordCount(final AccessibilityEvent accessibilityEvent) {
        return accessibilityEvent.getRecordCount();
    }
}