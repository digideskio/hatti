(ns hatti.macros)

(defmacro read-file
 "Takes a filename, and returns contents as string, at compile time. Used for
  ClojureScript tests to read fixtures."
 [path filename]
 (slurp (str path filename)))
