/*
 * The IEEE hereby grants a general, royalty-free license to copy, distribute,
 * display and make derivative works from this material, for all purposes,
 * provided that any use of the material contains the following
 * attribution: "Reprinted with permission from IEEE 1516.1(TM)-2010".
 * Should you require additional information, contact the Manager, Standards
 * Intellectual Property, IEEE Standards Association (stds-ipr@ieee.org).
 */
package hla.rti1516e;

import hla.rti1516e.exceptions.RTIinternalError;
import java.util.logging.Logger;

//==============================================================================
// bergtwvd:
//
// The original IEEE RtiFactoryFactory implementation uses the ServiceRegistry.
// However, with later (> 8) Java runtimes this causes an exception:
//   Exception in thread "main" java.lang.IllegalArgumentException: hla.rti1516e.RtiFactory is not an ImageIO SPI class
//
// The solution is to use the ServiceLoader instead of the ServiceRegistry.
//
// The RtiFactoryFactory class in this file uses the ServiceLoader.
//==============================================================================
import java.util.ServiceLoader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.Set;
import java.util.logging.Level;

/**
 * Helper class for locating RtiFactory. Uses Service concept described by
 * ServiceLoader.
 */
public class RtiFactoryFactory {

	public static RtiFactory getRtiFactory(String name) throws RTIinternalError {
		ServiceLoader<RtiFactory> loader = ServiceLoader.load(RtiFactory.class);
		final Iterator<RtiFactory> it = loader.iterator();
		while (it.hasNext()) {
			try {
				RtiFactory rtiFactory = it.next();
				if (rtiFactory.rtiName().equals(name)) {
					return rtiFactory;
				}
			} catch (UnsatisfiedLinkError | ServiceConfigurationError ex) {
				Logger.getLogger(RtiFactoryFactory.class.getName()).log(Level.WARNING, ex.getMessage());
			}
		}
		throw new RTIinternalError("Cannot find factory matching " + name);
	}

	public static RtiFactory getRtiFactory() throws RTIinternalError {
		ServiceLoader<RtiFactory> loader = ServiceLoader.load(RtiFactory.class);
		final Iterator<RtiFactory> it = loader.iterator();
		while (it.hasNext()) {
			try {
				return it.next();
			} catch (UnsatisfiedLinkError | ServiceConfigurationError ex) {
				Logger.getLogger(RtiFactoryFactory.class.getName()).log(Level.WARNING, ex.getMessage());
			}
		}
		throw new RTIinternalError("Cannot find factory");
	}

	public static Set<RtiFactory> getAvailableRtiFactories() {
		Set<RtiFactory> factories = new HashSet<>();
		ServiceLoader<RtiFactory> loader = ServiceLoader.load(RtiFactory.class);

		final Iterator<RtiFactory> it = loader.iterator();
		while (it.hasNext()) {
			try {
				factories.add(it.next());
			} catch (UnsatisfiedLinkError | ServiceConfigurationError ex) {
				Logger.getLogger(RtiFactoryFactory.class.getName()).log(Level.WARNING, ex.getMessage());
			}
		}

		return factories;
	}
}
