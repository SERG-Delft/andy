package nl.tudelft.cse1110.andy;

import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.result.Result;

public class Andy {

    public Result run(Context ctx) {
        return ExecutionFlow
                .build(ctx)
                .run();
    }

}
