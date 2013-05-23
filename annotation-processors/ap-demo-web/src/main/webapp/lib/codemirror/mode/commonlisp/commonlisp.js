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
CodeMirror.defineMode("commonlisp", function (config) {
  var assumeBody = /^with|^def|^do|^prog|case$|^cond$|bind$|when$|unless$/;
  var numLiteral = /^(?:[+\-]?(?:\d+|\d*\.\d+)(?:[efd][+\-]?\d+)?|[+\-]?\d+(?:\/[+\-]?\d+)?|#b[+\-]?[01]+|#o[+\-]?[0-7]+|#x[+\-]?[\da-f]+)/;
  var symbol = /[^\s'`,@()\[\]";]/;
  var type;

  function readSym(stream) {
    var ch;
    while (ch = stream.next()) {
      if (ch == "\\") stream.next();
      else if (!symbol.test(ch)) { stream.backUp(1); break; }
    }
    return stream.current();
  }

  function base(stream, state) {
    if (stream.eatSpace()) {type = "ws"; return null;}
    if (stream.match(numLiteral)) return "number";
    var ch = stream.next();
    if (ch == "\\") ch = stream.next();

    if (ch == '"') return (state.tokenize = inString)(stream, state);
    else if (ch == "(") { type = "open"; return "bracket"; }
    else if (ch == ")" || ch == "]") { type = "close"; return "bracket"; }
    else if (ch == ";") { stream.skipToEnd(); type = "ws"; return "comment"; }
    else if (/['`,@]/.test(ch)) return null;
    else if (ch == "|") {
      if (stream.skipTo("|")) { stream.next(); return "symbol"; }
      else { stream.skipToEnd(); return "error"; }
    } else if (ch == "#") {
      var ch = stream.next();
      if (ch == "[") { type = "open"; return "bracket"; }
      else if (/[+\-=\.']/.test(ch)) return null;
      else if (/\d/.test(ch) && stream.match(/^\d*#/)) return null;
      else if (ch == "|") return (state.tokenize = inComment)(stream, state);
      else if (ch == ":") { readSym(stream); return "meta"; }
      else return "error";
    } else {
      var name = readSym(stream);
      if (name == ".") return null;
      type = "symbol";
      if (name == "nil" || name == "t") return "atom";
      if (name.charAt(0) == ":") return "keyword";
      if (name.charAt(0) == "&") return "variable-2";
      return "variable";
    }
  }

  function inString(stream, state) {
    var escaped = false, next;
    while (next = stream.next()) {
      if (next == '"' && !escaped) { state.tokenize = base; break; }
      escaped = !escaped && next == "\\";
    }
    return "string";
  }

  function inComment(stream, state) {
    var next, last;
    while (next = stream.next()) {
      if (next == "#" && last == "|") { state.tokenize = base; break; }
      last = next;
    }
    type = "ws";
    return "comment";
  }

  return {
    startState: function () {
      return {ctx: {prev: null, start: 0, indentTo: 0}, tokenize: base};
    },

    token: function (stream, state) {
      if (stream.sol() && typeof state.ctx.indentTo != "number")
        state.ctx.indentTo = state.ctx.start + 1;

      type = null;
      var style = state.tokenize(stream, state);
      if (type != "ws") {
        if (state.ctx.indentTo == null) {
          if (type == "symbol" && assumeBody.test(stream.current()))
            state.ctx.indentTo = state.ctx.start + config.indentUnit;
          else
            state.ctx.indentTo = "next";
        } else if (state.ctx.indentTo == "next") {
          state.ctx.indentTo = stream.column();
        }
      }
      if (type == "open") state.ctx = {prev: state.ctx, start: stream.column(), indentTo: null};
      else if (type == "close") state.ctx = state.ctx.prev || state.ctx;
      return style;
    },

    indent: function (state, _textAfter) {
      var i = state.ctx.indentTo;
      return typeof i == "number" ? i : state.ctx.start + 1;
    },

    lineComment: ";;",
    blockCommentStart: "#|",
    blockCommentEnd: "|#"
  };
});

CodeMirror.defineMIME("text/x-common-lisp", "commonlisp");
