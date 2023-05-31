In this exercise, you will be testing the `calculate` method.

> This method calculates the tax income, based on the received income. The income is taxed as follows:
>
- `0 <= Income < 22100` → **Tax = `0.15 * Income`**  
- `22100 <= Income < 53500` → **Tax = `3315 + 0.28 * (Income - 22100)`**  
- `53500 <= Income < 115000` → **Tax = `12107 + 0.31 * (Income - 53500)`**  
- `115000 <= Income < 250000` → **Tax = `31172 + 0.36 * (Income - 115000)`**  
- `250000 <= Income` → **Tax = `79772 + 0.396 * (Income - 250000)`** 

Use **property-based testing** to create a suitable test suite for this method.

*This method was taken from: Kaner, C., Padmanabhan, S., & Hoffman, D. (2013). The Domain Testing Workbook. Context Driven Press.**