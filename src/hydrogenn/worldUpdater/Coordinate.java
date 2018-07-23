package hydrogenn.worldUpdater;

public class Coordinate {

	private int x;

	private int z;
	
	private Direction direction;
	

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}
	
	public Coordinate() {
		this.x = 0;
		this.z = 0;
		this.direction = Direction.NORTH;
	}
	
	public Coordinate(int start) {
		this.x = start;
		this.z = 0;
		this.direction = Direction.NORTH;
	}
	
	private Coordinate(int x, int z, Direction direction) {
		this.x = x;
		this.z = z;
		this.direction = direction;
	}

	public void keepMoving() {
		Direction effectiveDirection = direction;
		if (move(effectiveDirection).greatestDistance() < greatestDistance() || (x == -z && direction == Direction.WEST)) {
				effectiveDirection = effectiveDirection.rotate_counter();
		}
		Coordinate newCoordinate = move(effectiveDirection);
		this.x = newCoordinate.getX();
		this.z = newCoordinate.getZ();
		this.direction = newCoordinate.direction;
	}
	
	public Coordinate move(Direction direction) {
		return new Coordinate(x + direction.xOffset(), z + direction.zOffset(), direction.rotate());
	}

	public int greatestDistance() {
		return Math.max(Math.abs(x), Math.abs(z));
	}
	
}
