package traceable.instances

import traceable.core.{TraceableCoreOps}
import traceable.syntax.TraceableSyntax

/**
 * Created by lucatosatto on 6/28/16.
 */
object ConsoleTraceable extends Recorders.ConsolePrinter with TraceableCoreOps with TraceableSyntax { }


