/*
 * Copyright (c) 2013, Klaus Boeing & Michel Graciano.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the project's authors nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND/OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
CodeMirror.defineMode("apl", function() {
  var builtInOps = {
    ".": "innerProduct",
    "\\": "scan",
    "/": "reduce",
    "⌿": "reduce1Axis",
    "⍀": "scan1Axis",
    "¨": "each",
    "⍣": "power"
  };
  var builtInFuncs = {
    "+": ["conjugate", "add"],
    "−": ["negate", "subtract"],
    "×": ["signOf", "multiply"],
    "÷": ["reciprocal", "divide"],
    "⌈": ["ceiling", "greaterOf"],
    "⌊": ["floor", "lesserOf"],
    "∣": ["absolute", "residue"],
    "⍳": ["indexGenerate", "indexOf"],
    "?": ["roll", "deal"],
    "⋆": ["exponentiate", "toThePowerOf"],
    "⍟": ["naturalLog", "logToTheBase"],
    "○": ["piTimes", "circularFuncs"],
    "!": ["factorial", "binomial"],
    "⌹": ["matrixInverse", "matrixDivide"],
    "<": [null, "lessThan"],
    "≤": [null, "lessThanOrEqual"],
    "=": [null, "equals"],
    ">": [null, "greaterThan"],
    "≥": [null, "greaterThanOrEqual"],
    "≠": [null, "notEqual"],
    "≡": ["depth", "match"],
    "≢": [null, "notMatch"],
    "∈": ["enlist", "membership"],
    "⍷": [null, "find"],
    "∪": ["unique", "union"],
    "∩": [null, "intersection"],
    "∼": ["not", "without"],
    "∨": [null, "or"],
    "∧": [null, "and"],
    "⍱": [null, "nor"],
    "⍲": [null, "nand"],
    "⍴": ["shapeOf", "reshape"],
    ",": ["ravel", "catenate"],
    "⍪": [null, "firstAxisCatenate"],
    "⌽": ["reverse", "rotate"],
    "⊖": ["axis1Reverse", "axis1Rotate"],
    "⍉": ["transpose", null],
    "↑": ["first", "take"],
    "↓": [null, "drop"],
    "⊂": ["enclose", "partitionWithAxis"],
    "⊃": ["diclose", "pick"],
    "⌷": [null, "index"],
    "⍋": ["gradeUp", null],
    "⍒": ["gradeDown", null],
    "⊤": ["encode", null],
    "⊥": ["decode", null],
    "⍕": ["format", "formatByExample"],
    "⍎": ["execute", null],
    "⊣": ["stop", "left"],
    "⊢": ["pass", "right"]
  };

  var isOperator = /[\.\/⌿⍀¨⍣]/;
  var isNiladic = /⍬/;
  var isFunction = /[\+−×÷⌈⌊∣⍳\?⋆⍟○!⌹<≤=>≥≠≡≢∈⍷∪∩∼∨∧⍱⍲⍴,⍪⌽⊖⍉↑↓⊂⊃⌷⍋⍒⊤⊥⍕⍎⊣⊢]/;
  var isArrow = /←/;
  var isComment = /[⍝#].*$/;

  var stringEater = function(type) {
    var prev;
    prev = false;
    return function(c) {
      prev = c;
      if (c === type) {
        return prev === "\\";
      }
      return true;
    };
  };
  return {
    startState: function() {
      return {
        prev: false,
        func: false,
        op: false,
        string: false,
        escape: false
      };
    },
    token: function(stream, state) {
      var ch, funcName, word;
      if (stream.eatSpace()) {
        return null;
      }
      ch = stream.next();
      if (ch === '"' || ch === "'") {
        stream.eatWhile(stringEater(ch));
        stream.next();
        state.prev = true;
        return "string";
      }
      if (/[\[{\(]/.test(ch)) {
        state.prev = false;
        return null;
      }
      if (/[\]}\)]/.test(ch)) {
        state.prev = true;
        return null;
      }
      if (isNiladic.test(ch)) {
        state.prev = false;
        return "niladic";
      }
      if (/[¯\d]/.test(ch)) {
        if (state.func) {
          state.func = false;
          state.prev = false;
        } else {
          state.prev = true;
        }
        stream.eatWhile(/[\w\.]/);
        return "number";
      }
      if (isOperator.test(ch)) {
        return "operator apl-" + builtInOps[ch];
      }
      if (isArrow.test(ch)) {
        return "apl-arrow";
      }
      if (isFunction.test(ch)) {
        funcName = "apl-";
        if (builtInFuncs[ch] != null) {
          if (state.prev) {
            funcName += builtInFuncs[ch][1];
          } else {
            funcName += builtInFuncs[ch][0];
          }
        }
        state.func = true;
        state.prev = false;
        return "function " + funcName;
      }
      if (isComment.test(ch)) {
        stream.skipToEnd();
        return "comment";
      }
      if (ch === "∘" && stream.peek() === ".") {
        stream.next();
        return "function jot-dot";
      }
      stream.eatWhile(/[\w\$_]/);
      word = stream.current();
      state.prev = true;
      return "keyword";
    }
  };
});

CodeMirror.defineMIME("text/apl", "apl");
