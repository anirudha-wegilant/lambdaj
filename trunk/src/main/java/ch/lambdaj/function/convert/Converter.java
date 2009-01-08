// Modified or written by Ex Machina SAGL for inclusion with lambdaj.
// Copyright (c) 2009 Mario Fusco, Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.convert;

/**
 * @author Mario Fusco
 */
public interface Converter<F, T> {

	public T convert(F from);
}
