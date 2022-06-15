package net.sp8craft.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ConditionalList<T> implements List<T> {
    private final List<T> conditionData;
    private final List<T> defaultData;
    private final ConditionFunc func;


    public ConditionalList(List<T> conditionData, List<T> defaultData, ConditionFunc func, T fillerElement) {
        if(conditionData.size() != defaultData.size() && fillerElement != null) {
            List<T> smallerList;
            int diff;

            if(conditionData.size() > defaultData.size()) {
                smallerList = defaultData;
                diff = conditionData.size() - defaultData.size();
            } else {
                smallerList = conditionData;
                diff = defaultData.size() - conditionData.size();
            }

            for(int i = 0;i<Math.abs(diff);i++) {
                smallerList.add(fillerElement);
            }
        }

        if (conditionData.size() != defaultData.size() &&  fillerElement == null) {
            throw new RuntimeException("Error creating list, sizes must match or specify filler constructor!");
        }

        this.conditionData = conditionData;
        this.defaultData = defaultData;
        this.func = func;
    }

    public ConditionalList(List<T> conditionData, List<T> data2, ConditionFunc func) {
        this(conditionData, data2, func, null);
    }

    public interface ConditionFunc {
        boolean isConditionMet();
    }

    private List<T> getData() {
        if (this.func.isConditionMet()) {
            return this.conditionData;
        } else {
            return this.defaultData;
        }
    }

    @Override
    public int size() {
        return getData().size();
    }

    @Override
    public boolean isEmpty() {
        return this.getData().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.getData().contains(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return this.getData().iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return this.getData().toArray();
    }

    @NotNull
    @Override
    public <T1> T1[] toArray(@NotNull T1[] a) {
        return this.getData().toArray(a);
    }

    @Override
    public boolean add(T t) {
        return this.getData().add(t);
    }

    @Override
    public boolean remove(Object o) {
        return this.getData().remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return this.getData().containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return this.getData().addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        return this.getData().addAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return this.getData().removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return this.getData().retainAll(c);
    }

    @Override
    public void clear() {
        this.getData().clear();
    }

    @Override
    public T get(int index) {
        return this.getData().get(index);
    }

    @Override
    public T set(int index, T element) {
        return this.getData().set(index, element);
    }

    @Override
    public void add(int index, T element) {
        this.getData().add(index, element);
    }

    @Override
    public T remove(int index) {
        return this.getData().remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.getData().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.getData().lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        return this.getData().listIterator();
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        return this.getData().listIterator(index);
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return this.getData().subList(fromIndex, toIndex);
    }
}