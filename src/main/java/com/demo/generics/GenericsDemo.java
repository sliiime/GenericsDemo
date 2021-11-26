package com.demo.generics;

public class GenericsDemo {

	private static class Animal {

	}

	private static class Cat extends Animal {

	}

	private static class Dog extends Animal {

	}

	private static class Container<T> {

		T value;

		public void set(T value) {
			this.value = value;
		}

		public T get() {
			return value;
		}
	}

	public static void main(String[] args) {

		Container<Animal> animals = new Container<>();
		Container<Cat> cats = new Container<>();
		Container<Dog> dogs = new Container<>();

		// ---------- using a producer ----------
		// incorrect_vaccinateAnimals(cats);// does not compile!!
		vaccinateAnimals(cats); // this is great, cats container can give Animals
		vaccinateAnimals(dogs); // this is great, dogs container can give Animals
		vaccinateAnimals(animals); // this is great, animals container can give Animals

		// ---------- using a consumer ----------
		// incorrect_addCatToAnyContainerThatCanHaveCats(animals); // does not compile!!
		addCatToAnyContainerThatCanHaveCats(cats); // this is great, cats container can also host cats
		addCatToAnyContainerThatCanHaveCats(animals); // this is great, animals container can also host cats

		// call parameterized method, which infers the type of T on runtime
		doesntKnowWhatTheContainerIsAtCompileTime(cats);
		doesntKnowWhatTheContainerIsAtCompileTime(dogs);
		doesntKnowWhatTheContainerIsAtCompileTime(animals);
	}

	// ---------------- producer -----------------

	// This is not flexible enough. It won't allow to be called for Container<Cats>, even though Cats are Animals.
	private static void incorrect_vaccinateAnimals(Container<Animal> animals) {
		Animal animal = animals.get();
	}

	// This is much better. Container is a any producer of animals.
	private static void vaccinateAnimals(Container<? extends Animal> animals) {
		Animal animal = animals.get();
//		animals.set(); compiler does not allow to add anything to the container because it doesn't know its type, the container effectively becomes read-only
	}

	// --------------- consumer --------------------

	// This is not flexible enough. It won't allow to be called for Container<Animal>, even though a Container<Animal> can host Cats.
	private static void incorrect_addCatToAnyContainerThatCanHaveCats(Container<Cat> cats) {
		cats.set(new Cat());
	}

	// This is much better. Container is a consumer of Cats.
	private static void addCatToAnyContainerThatCanHaveCats(Container<? super Cat> cats) {
		cats.set(new Cat());
		Object object = cats.get();// the compiler doesn't know the type
									// of the items in the container, it could have anything
									// which is a superclass of a Cat, so we can only assume it's an Object
	}

	// parameterized method
	private static <T extends Animal> void doesntKnowWhatTheContainerIsAtCompileTime(Container<T> c) {
		Animal t = c.get(); // great I can get animals
		T t1 = c.get(); // great I can get T but I don't know what T is at compile time
		// c.set(new Animal()); // doesn't compile!!
		// c.set(new Cat()); // doesn't compile!!
		// c.set(new Dog()); // doesn't compile!!

		T t3 = somehowGetAnObject(); // if you can somehow get a T
		c.set(t3); // then you can add it
	}

	private static <T> T somehowGetAnObject() {
		return null;
	}
}
