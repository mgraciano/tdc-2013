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
CodeMirror.tagRangeFinder = (function() {
  var nameStartChar = "A-Z_a-z\\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02FF\\u0370-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD";
  var nameChar = nameStartChar + "\-\:\.0-9\\u00B7\\u0300-\\u036F\\u203F-\\u2040";
  var xmlTagStart = new RegExp("<(/?)([" + nameStartChar + "][" + nameChar + "]*)", "g");

  return function(cm, start) {
    var line = start.line, ch = start.ch, lineText = cm.getLine(line);

    function nextLine() {
      if (line >= cm.lastLine()) return;
      ch = 0;
      lineText = cm.getLine(++line);
      return true;
    }
    function toTagEnd() {
      for (;;) {
        var gt = lineText.indexOf(">", ch);
        if (gt == -1) { if (nextLine()) continue; else return; }
        var lastSlash = lineText.lastIndexOf("/", gt);
        var selfClose = lastSlash > -1 && /^\s*$/.test(lineText.slice(lastSlash + 1, gt));
        ch = gt + 1;
        return selfClose ? "selfClose" : "regular";
      }
    }
    function toNextTag() {
      for (;;) {
        xmlTagStart.lastIndex = ch;
        var found = xmlTagStart.exec(lineText);
        if (!found) { if (nextLine()) continue; else return; }
        ch = found.index + found[0].length;
        return found;
      }
    }

    var stack = [], startCh;
    for (;;) {
      var openTag = toNextTag(), end;
      if (!openTag || line != start.line || !(end = toTagEnd())) return;
      if (!openTag[1] && end != "selfClose") {
        stack.push(openTag[2]);
        startCh = ch;
        break;
      }
    }

    for (;;) {
      var next = toNextTag(), end, tagLine = line, tagCh = ch - (next ? next[0].length : 0);
      if (!next || !(end = toTagEnd())) return;
      if (end == "selfClose") continue;
      if (next[1]) { // closing tag
        for (var i = stack.length - 1; i >= 0; --i) if (stack[i] == next[2]) {
          stack.length = i;
          break;
        }
        if (!stack.length) return {
          from: CodeMirror.Pos(start.line, startCh),
          to: CodeMirror.Pos(tagLine, tagCh)
        };
      } else { // opening tag
        stack.push(next[2]);
      }
    }
  };
})();
