package me.Minecraftmage113.InsanityPlugin.helpers;

public class Pair<A, B> {
	A key;
	B object;
	public Pair(A key, B object) {
		this.key = key;
		this.object = object;
	}
	public A first(){ return key; }
	public B second() { return object; }
}
