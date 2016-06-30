package traceable

import traceable.core.TraceableOps
import traceable.syntax.TraceableSyntax


object TestTraceable extends Recorders.TestGraphRecorder with TraceableOps with TraceableSyntax { }
