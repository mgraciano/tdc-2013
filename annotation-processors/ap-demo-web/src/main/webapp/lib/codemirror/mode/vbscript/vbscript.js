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
CodeMirror.defineMode("vbscript", function() {
  var regexVBScriptKeyword = /^(?:Call|Case|CDate|Clear|CInt|CLng|Const|CStr|Description|Dim|Do|Each|Else|ElseIf|End|Err|Error|Exit|False|For|Function|If|LCase|Loop|LTrim|Next|Nothing|Now|Number|On|Preserve|Quit|ReDim|Resume|RTrim|Select|Set|Sub|Then|To|Trim|True|UBound|UCase|Until|VbCr|VbCrLf|VbLf|VbTab)$/im;

  return {
    token: function(stream) {
      if (stream.eatSpace()) return null;
      var ch = stream.next();
      if (ch == "'") {
        stream.skipToEnd();
        return "comment";
      }
      if (ch == '"') {
        stream.skipTo('"');
        return "string";
      }

      if (/\w/.test(ch)) {
        stream.eatWhile(/\w/);
        if (regexVBScriptKeyword.test(stream.current())) return "keyword";
      }
      return null;
    }
  };
});

CodeMirror.defineMIME("text/vbscript", "vbscript");
