module hla.rti1516e {
	requires java.logging;

	exports hla.rti1516e;
	exports hla.rti1516e.encoding;
	exports hla.rti1516e.exceptions;
	exports hla.rti1516e.time;
	
	uses hla.rti1516e.RtiFactory;
	uses hla.rti1516e.LogicalTimeFactory;
}
