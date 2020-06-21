#import <Foundation/Foundation.h>

@interface ca_weblite_irblaster_IRBlasterNativeImpl : NSObject {
}

-(NSData*)getCarrierFrequencies;
-(void)transmit:(int)param param1:(NSData*)param1;
-(BOOL)isSupported;
@end
