/**
 * // Modified or written by Ex Machina SAGL for inclusion with lambdaj.
 * // Copyright (c) 2008 Mario Fusco, Luca Marrocco.
 * // Licensed under the Apache License, Version 2.0 (the "License")
 */
package ch.lambdaj.group;

import java.util.*;

@SuppressWarnings("serial")
public class GroupConditions extends LinkedList<GroupCondition> {

	public void by(String by) {
		add(new GroupCondition(by));
	}

	public void as(String alias) {
		getLast().setAlias("insureds");
	}

	public void head(String property) {
		getLast().addProperty(property);
	}

	public void head(String property, String alias) {
		getLast().addProperty(property, alias);
	}
}
