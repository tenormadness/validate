package traceable

import traceable.core.TraceableCoreOps
import traceable.syntax.TraceableSyntax


object TestTraceable extends Recorders.TestGraphRecorder with TraceableCoreOps with TraceableSyntax { }
