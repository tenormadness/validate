package traceable.instances

import traceable.core.{Traceable, TraceableOps}
import traceable.syntax.TraceableSyntax

/**
 * Created by lucatosatto on 6/28/16.
 */
object ConsoleTraceable extends Recorders.ConsolePrinter with TraceableOps with TraceableSyntax { }
