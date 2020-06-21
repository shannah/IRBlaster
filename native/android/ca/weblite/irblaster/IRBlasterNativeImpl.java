package ca.weblite.irblaster;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.hardware.ConsumerIrManager.CarrierFrequencyRange;
import com.codename1.impl.android.AndroidNativeUtil;

public class IRBlasterNativeImpl {
    private ConsumerIrManager irManager;
    
    private ConsumerIrManager irManager() {
        if (irManager == null) {
            irManager = (ConsumerIrManager)AndroidNativeUtil.getActivity().getSystemService(Context.CONSUMER_IR_SERVICE);
        }
        return irManager;
    }
    
    public int[] getCarrierFrequencies() {
        CarrierFrequencyRange[] ranges =  irManager.getCarrierFrequencies();
        int len = ranges.length;
        int[] out = new int[len*2];
        for (int i=0; i<len; i++) {
            int j = i * 2;
            out[j] = ranges[i].getMinFrequency();
            out[j+1] = ranges[i].getMaxFrequency();
        }
        return out;
    }

    public void transmit(int carrierFrequency, int[] code) {
        irManager.transmit(carrierFrequency, code);
    }

    public boolean isSupported() {
        return irManager() != null && irManager().hasIrEmitter();
    }

}
