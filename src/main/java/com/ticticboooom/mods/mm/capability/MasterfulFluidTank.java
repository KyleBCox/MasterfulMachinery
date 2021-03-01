package com.ticticboooom.mods.mm.capability;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MasterfulFluidTank implements IFluidHandler {
    private final int capacity;
    private final int extractSpeed;
    private final int insertSpeed;
    private FluidStack stack;
    private final List<Consumer<Integer>> listeners = new ArrayList<>();

    public MasterfulFluidTank(int capacity, int extractSpeed, int insertSpeed) {
        stack = FluidStack.EMPTY;
        this.capacity = capacity;
        this.extractSpeed = extractSpeed;
        this.insertSpeed = insertSpeed;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        if (tank > 0) {
            return FluidStack.EMPTY;
        }
        return stack;
    }


    @Override
    public int getTankCapacity(int tank) {
        if (tank > 0) {
            return 0;
        }
        return capacity;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        if (tank > 0) {
            return false;
        }
        if (this.stack.isEmpty()) {
            return true;
        }
        return stack.isFluidEqual(this.stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (isFluidValid(0, resource)) {
            int sizeToBe = resource.getAmount() + this.stack.getAmount();
            if (resource.getAmount() > insertSpeed) {
                if (insertSpeed + this.stack.getAmount() > this.capacity) {
                    if (action.execute()) {
                        setAmount(resource, this.capacity);
                    }
                    return insertSpeed - this.capacity;
                }
                int remainder = resource.getAmount() - insertSpeed;
                if (action.execute()) {
                    setAmount(resource, stack.getAmount() + insertSpeed);
                }
                return remainder;
            }
            if (sizeToBe > this.capacity) {
                if (action.execute()) {
                    setAmount(resource, this.capacity);
                }
                return sizeToBe - this.capacity;
            }
            if (action.execute()) {
                setAmount(resource, resource.getAmount() + this.stack.getAmount());
            }
            return 0;
        }
        return resource.getAmount();
    }

    private void setAmount(FluidStack resource, int amount) {
        if (this.stack.getFluid() == resource.getFluid()) {
            this.stack.setAmount(amount);
        } else {
            this.stack = new FluidStack(resource.getFluid(), amount);
        }
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (isFluidValid(0, resource)) {
            int difference = stack.getAmount() - resource.getAmount();
            if (resource.getAmount() > extractSpeed) {
                difference = stack.getAmount() - extractSpeed;
                if (difference < 0) {
                    if (action.execute()) {
                        setAmount(resource, 0);
                    }
                    return new FluidStack(resource.getFluid(), extractSpeed - (difference * -1));
                }
                if (action.execute()) {
                    setAmount(resource, stack.getAmount() - extractSpeed);
                }
                return new FluidStack(resource.getFluid(), extractSpeed);
            }
            if (difference < 0) {
                if (action.execute()) {
                    setAmount(resource, 0);
                }
                return new FluidStack(resource.getFluid(), difference * -1);
            }
            if (action.execute()) {
                setAmount(resource, stack.getAmount() - resource.getAmount());
            }
            return new FluidStack(stack.getFluid(), resource.getAmount());
        }
        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        int difference = stack.getAmount() - maxDrain;
        if (maxDrain > extractSpeed) {
            difference = stack.getAmount() - extractSpeed;

            if (difference < 0) {
                if (action.execute()) {
                    stack.setAmount(0);
                }
                return new FluidStack(stack.getFluid(), extractSpeed);
            }
            if (action.execute()) {
                stack.setAmount(stack.getAmount() - extractSpeed);
            }
        }
        if (difference < 0) {
            if (action.execute()) {
                stack.setAmount(0);
            }
            return new FluidStack(stack.getFluid(), difference * -1);
        }
        if (action.execute()) {
            stack.setAmount(stack.getAmount() - maxDrain);
        }
        return new FluidStack(stack.getFluid(), maxDrain);
    }

    public FluidStack getStack() {
        return this.stack;
    }

    public void setStack(FluidStack stack) {
        this.stack = stack;
    }

}
