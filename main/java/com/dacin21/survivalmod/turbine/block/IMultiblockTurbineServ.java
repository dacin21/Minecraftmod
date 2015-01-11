package com.dacin21.survivalmod.turbine.block;

public interface IMultiblockTurbineServ {
	public abstract TileTurbineRotorbase getMaster();
	public abstract int[] getMasterCoords();
	public abstract void setMasterCoords(int newX, int newY, int newZ);
	public abstract void initFromMaster(TileTurbineRotorbase masterTile);
	public abstract void unloadMaster();
	public abstract boolean hasMaster();
}
