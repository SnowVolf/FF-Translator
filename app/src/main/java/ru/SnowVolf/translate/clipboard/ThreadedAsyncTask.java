/*
 * Copyright (c) 2017 Snow Volf (Artem Zhiganov).
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.SnowVolf.translate.clipboard;


import android.os.AsyncTask;

/**
 * Run AsynTask in parallel
 * @see <a href="http://stackoverflow.com/a/19060929/4468645">Stack Overflow</a>
 */
public abstract class ThreadedAsyncTask<Params, Progress, Result> extends
        AsyncTask<Params, Progress, Result> {

    /**
     * Call this instead of execute() for parallel execution
     * @param params - passed in to doInBackground
     * @return This instance of AsyncTask
     */
    @SafeVarargs
    public final AsyncTask<Params, Progress, Result> executeMe(Params... params){
        return super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }
}

