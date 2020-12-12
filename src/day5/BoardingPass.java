package day5;

public class BoardingPass {
    private int row;
    private int column;

    public BoardingPass(String code) {
        this.setRow(code.substring(0, 7));
        this.setColumn(code.substring(7));
    }

    private void setRow(String code) {
        int min = 0;
        int max = 127;

        for (int i = 0; i < code.length(); ++i) {
            int diff = (int) Math.ceil((max - min) / 2.0);
            if (code.charAt(i) == 'F') {
                max -= diff;
            } else {
                min += diff;
            }
        }

        this.row = min;
    }

    private void setColumn(String code) {
        int min = 0;
        int max = 7;

        for (int i = 0; i < code.length(); ++i) {
            int diff = (int) Math.ceil((max - min) / 2.0);
            if (code.charAt(i) == 'L') {
                max -= diff;
            } else {
                min += diff;
            }
        }

        this.column = min;
    }

    public int getSeatID() {
        return row * 8 + column;
    }
}
