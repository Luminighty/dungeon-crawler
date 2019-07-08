/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Iterator;

/**
 *
 * @author Luminight
 */
public class OrderedList<T> implements Iterable<T> {

	private class ListElement<T> {

		public int Priority;
		public T Key;
		public ListElement<T> Next;

		public ListElement(T Key, int prior) {
			this.Key = Key;
			this.Priority = prior;
		}
	}

	public ListElement<T> head = null;

	public OrderedList() {
	}

	public void add(T element, int priority) {
		if (head == null || head.Priority <= priority) {
			ListElement<T> p = new ListElement<T>(element, priority);
			p.Next = head;
			head = p;
		} else {
			ListElement<T> p = head;
			while (p.Next != null && p.Next.Priority > priority) {
				p = p.Next;
			}
			ListElement<T> e = new ListElement<T>(element, priority);
			e.Next = p.Next;
			p.Next = e;
		}
	}

	public void clear() {
		while (head != null) {
			head = head.Next;
		}
	}

	public void remove(T element) {
		if (head == null) {
			return;
		}
		if (head.equals(element)) {
			head = head.Next;
			return;
		}
		ListElement<T> p = head;
		while (p.Next != null && !p.Next.Key.equals(element)) {
			p = p.Next;
		}
		if (p.Next != null) {
			p.Next = p.Next.Next;
		}
	}

	@Override
	public Iterator<T> iterator() {
		//return new OrderedListIterator<T>(head);
		return new Iterator<T>() {

			ListElement<T> p = head;

			@Override
			public boolean hasNext() {
				return p != null;
			}

			@Override
			public T next() {
				T key = p.Key;
				p = p.Next;
				return key;
			}
		};
	}

}
