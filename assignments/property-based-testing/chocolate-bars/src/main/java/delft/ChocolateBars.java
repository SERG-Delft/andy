package delft;

class ChocolateBars {

	public static final int CANNOT_PACK_BAG = -1;

	public int calculate(int small, int big, int total) {
		int maxBigBoxes = total / 5;
		int bigBoxesWeCanUse = Math.min(maxBigBoxes, big);
		total -= (bigBoxesWeCanUse * 5);
		if (small < total)
			return CANNOT_PACK_BAG;
		return total;
	}
}
