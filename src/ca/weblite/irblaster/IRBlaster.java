/*
 * Copyright 2020 Web Lite Solutions Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.weblite.irblaster;

import com.codename1.io.Log;
import com.codename1.system.NativeLookup;

/**
 * Provides access to the device's IR blaster if it has one.  Currently this is only 
 * supported on Android, and acts as a wrapper around the https://developer.android.com/reference/android/hardware/ConsumerIrManager[ConsumerIrManager] class.
 * 
 * === Usage Example
 * 
 * [source,java]
 * ----
 * if (IRBlaster.hasIrEmitter()) {
 *     int frequency = 38000;
 *     int[] codes = new int[]{5, 6, 7, ...};
 *     IRBlaster.transmit(frequency, codes);
 * }
 * ----
 * 
 * @author Steve Hannah
 */
public class IRBlaster {
    
    private static IRBlasterNative peer;
    private static boolean failed;
    
    private static IRBlasterNative peer() {
        if (peer == null) {
            try {
                peer = NativeLookup.create(IRBlasterNative.class);
            } catch (Throwable t) {
                Log.e(t);
                failed = true;
            }
        }
        return peer;
    }
    
    /**
     * Represents a range of carrier frequencies (inclusive) on which the infrared transmitter can transmit 
     */
    public static class CarrierFrequencyRange {

        private CarrierFrequencyRange(int min, int max) {
            minFrequency = min;
            maxFrequency = max;
        }
        
        /**
         * @return the minFrequency
         */
        public int getMinFrequency() {
            return minFrequency;
        }

        /**
         * @return the maxFrequency
         */
        public int getMaxFrequency() {
            return maxFrequency;
        }
        private int minFrequency, maxFrequency;
    }
    
    /**
     * Check whether the device has an infrared emitter.
     * @return True if the device has IR emitter.
     */
    public static boolean hasIrEmitter() {
        IRBlasterNative b = peer();
        return !failed && b.isSupported();
                
    }
    
    /**
     * Query the infrared transmitter's supported carrier frequencies
     * @return 
     */
    public static CarrierFrequencyRange[] getCarrierFrequencies() {
        if (hasIrEmitter()) {
            int[] freqs = peer.getCarrierFrequencies();
            CarrierFrequencyRange[] out = new CarrierFrequencyRange[freqs.length/2];
            int len = out.length;
            for (int i=0; i<len; i++) {
                int j = i * 2;
                out[i] = new CarrierFrequencyRange(freqs[j], freqs[j+1]);
            }
            return out;
        }
        throw new IllegalStateException("This device doesn't support IR");
    }
    
    /**
     * Transmit an infrared pattern
     * This method is synchronous; when it returns the pattern has been transmitted.
     * 
     * @param carrierFrequency  The IR carrier frequency in Hertz.
     * @param pattern The alternating on/off pattern in microseconds to transmit.
     */
    public static void transmit(int carrierFrequency, int[] pattern) {
        if (hasIrEmitter()) {
            peer().transmit(carrierFrequency, pattern);
        }
    }
}
