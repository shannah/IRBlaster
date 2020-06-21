/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.weblite.irblaster;

import com.codename1.system.NativeInterface;

/**
 *
 * @author shannah
 */
public interface IRBlasterNative extends NativeInterface {
    public void transmit(int carrierFrequency, int[] pattern);
    public int[] getCarrierFrequencies();
}
