package com.progressoft.brix.domino.apt.commons;

public class ModifierBuilder {

    @FunctionalInterface
    interface ModifierWriter {
        String writeModifiers();
    }

    public class FieldAssignmentModifier implements ModifierWriter {

        private final StringBuilder modifiers = new StringBuilder();

        private FieldAssignmentModifier(String accessLevelModifier) {
            modifiers.append(accessLevelModifier);
        }

        public FieldAssignmentModifier staticModifier() {
            modifiers.append("static ");
            return this;
        }

        public FieldAssignmentModifier finalModifier() {
            modifiers.append("final ");
            return this;
        }

        @Override
        public String writeModifiers() {
            return modifiers.toString();
        }
    }

    public class InheritanceModifier implements ModifierWriter {

        private final StringBuilder modifiers = new StringBuilder();

        private InheritanceModifier(String accessLevelModifier) {
            modifiers.append(accessLevelModifier);
        }

        public FieldAssignmentModifier get() {
            return new FieldAssignmentModifier(modifiers.toString());
        }

        public ModifierWriter asAbstract() {
            modifiers.append("abstract ");
            return this;
        }

        @Override
        public String writeModifiers() {
            return modifiers.toString();
        }
    }

    public InheritanceModifier asPublic() {
        return new InheritanceModifier("public ");
    }

    public InheritanceModifier asPrivate() {
        return new InheritanceModifier("private ");
    }

    public InheritanceModifier asProtected() {
        return new InheritanceModifier("protected ");
    }

    public InheritanceModifier get() {
        return new InheritanceModifier("");
    }
}
