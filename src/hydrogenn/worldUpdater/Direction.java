package hydrogenn.worldUpdater;

public enum Direction {

	NORTH(1,0),
	WEST(0,1),
	SOUTH(-1,0),
	EAST(0,-1),
	WHAT(1,1);

	int x;
	int z;
	
	private Direction(int x, int z) {
		this.x = x;
		this.z = z;
	}
	
	public int xOffset() {
		return x;
	}

	public int zOffset() {
		return z;
	}

	public Direction rotate() {
		switch (this) {
		case NORTH:
			return WEST;
		case WEST:
			return SOUTH;
		case SOUTH:
			return EAST;
		case EAST:
			return NORTH;
		default:
			return WHAT;
		}
			
	}

	public Direction rotate_counter() {
		switch (this) {
		case NORTH:
			return EAST;
		case WEST:
			return NORTH;
		case SOUTH:
			return WEST;
		case EAST:
			return SOUTH;
		default:
			return WHAT;
		}
	}
	
	
	
}
