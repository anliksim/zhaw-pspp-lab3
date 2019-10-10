package ch.zhaw.anliksim.program;

interface Program {

    /**
     * Maps to {@link de.inetsoftware.jwebassembly.module.Emitter#emit()}
     */
    @SuppressWarnings("unused")
    double main(double arg);
}
