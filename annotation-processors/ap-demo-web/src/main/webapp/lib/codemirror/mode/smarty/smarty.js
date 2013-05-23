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
CodeMirror.defineMode("smarty", function(config) {
  var keyFuncs = ["debug", "extends", "function", "include", "literal"];
  var last;
  var regs = {
    operatorChars: /[+\-*&%=<>!?]/,
    validIdentifier: /[a-zA-Z0-9\_]/,
    stringChar: /[\'\"]/
  };
  var leftDelim = (typeof config.mode.leftDelimiter != 'undefined') ? config.mode.leftDelimiter : "{";
  var rightDelim = (typeof config.mode.rightDelimiter != 'undefined') ? config.mode.rightDelimiter : "}";
  function ret(style, lst) { last = lst; return style; }


  function tokenizer(stream, state) {
    function chain(parser) {
      state.tokenize = parser;
      return parser(stream, state);
    }

    if (stream.match(leftDelim, true)) {
      if (stream.eat("*")) {
        return chain(inBlock("comment", "*" + rightDelim));
      }
      else {
        state.tokenize = inSmarty;
        return "tag";
      }
    }
    else {
      // I'd like to do an eatWhile() here, but I can't get it to eat only up to the rightDelim string/char
      stream.next();
      return null;
    }
  }

  function inSmarty(stream, state) {
    if (stream.match(rightDelim, true)) {
      state.tokenize = tokenizer;
      return ret("tag", null);
    }

    var ch = stream.next();
    if (ch == "$") {
      stream.eatWhile(regs.validIdentifier);
      return ret("variable-2", "variable");
    }
    else if (ch == ".") {
      return ret("operator", "property");
    }
    else if (regs.stringChar.test(ch)) {
      state.tokenize = inAttribute(ch);
      return ret("string", "string");
    }
    else if (regs.operatorChars.test(ch)) {
      stream.eatWhile(regs.operatorChars);
      return ret("operator", "operator");
    }
    else if (ch == "[" || ch == "]") {
      return ret("bracket", "bracket");
    }
    else if (/\d/.test(ch)) {
      stream.eatWhile(/\d/);
      return ret("number", "number");
    }
    else {
      if (state.last == "variable") {
        if (ch == "@") {
          stream.eatWhile(regs.validIdentifier);
          return ret("property", "property");
        }
        else if (ch == "|") {
          stream.eatWhile(regs.validIdentifier);
          return ret("qualifier", "modifier");
        }
      }
      else if (state.last == "whitespace") {
        stream.eatWhile(regs.validIdentifier);
        return ret("attribute", "modifier");
      }
      else if (state.last == "property") {
        stream.eatWhile(regs.validIdentifier);
        return ret("property", null);
      }
      else if (/\s/.test(ch)) {
        last = "whitespace";
        return null;
      }

      var str = "";
      if (ch != "/") {
        str += ch;
      }
      var c = "";
      while ((c = stream.eat(regs.validIdentifier))) {
        str += c;
      }
      var i, j;
      for (i=0, j=keyFuncs.length; i<j; i++) {
        if (keyFuncs[i] == str) {
          return ret("keyword", "keyword");
        }
      }
      if (/\s/.test(ch)) {
        return null;
      }
      return ret("tag", "tag");
    }
  }

  function inAttribute(quote) {
    return function(stream, state) {
      while (!stream.eol()) {
        if (stream.next() == quote) {
          state.tokenize = inSmarty;
          break;
        }
      }
      return "string";
    };
  }

  function inBlock(style, terminator) {
    return function(stream, state) {
      while (!stream.eol()) {
        if (stream.match(terminator)) {
          state.tokenize = tokenizer;
          break;
        }
        stream.next();
      }
      return style;
    };
  }

  return {
    startState: function() {
      return { tokenize: tokenizer, mode: "smarty", last: null };
    },
    token: function(stream, state) {
      var style = state.tokenize(stream, state);
      state.last = last;
      return style;
    },
    electricChars: ""
  };
});

CodeMirror.defineMIME("text/x-smarty", "smarty");
