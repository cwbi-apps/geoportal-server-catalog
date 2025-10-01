define([
  "dojo/_base/declare",
  "app/common/Templated",
  "dojo/text!./templates/ShowToken.html",
  "dojo/i18n!app/nls/resources",
  "app/common/ModalDialog",
  "app/context/AppClient"
], function(
  declare, Templated, template, i18n, ModalDialog, AppClient
) {

  var oThisClass = declare([Templated], {

    i18n: i18n,
    templateString: template,

    messageNode: null,
    validationErrorsNode: null,

    postCreate: function() {
      this.inherited(arguments);
    },

    _copyToClipboard: function() {
      if (this.messageNode) {
        var text = this.messageNode.value || "";
        if (navigator.clipboard && navigator.clipboard.writeText) {
          navigator.clipboard.writeText(text).catch(function(err) {
            console.warn("Clipboard copy failed", err);
          });
        } else {
          // fallback for older browsers
          this.messageNode.select();
          try {
            document.execCommand("copy");
          } catch (err) {
            console.warn("Clipboard fallback failed", err);
          }
        }
      }
    },

    show: function(message) {
      var self = this, dialog = null;
      var client = new AppClient();

      var message = client.getAccessToken();
      if (message && this.messageNode) {
        this.messageNode.value = message;
      }

      dialog = new ModalDialog({
        content: this.domNode,
        title: i18n.content.showToken.caption,
        okLabel: i18n.general.ok,
        showCancel: false,
        onHide: function() {
          self.destroyRecursive(false);
        },
        onOkClicked: function() {
          dialog.hide();
        }
      });

      if (this.copyButtonNode) {
        this.copyButtonNode.onclick = function() {
          self._copyToClipboard();
        };
      }

      dialog.show();
    }

  });

  return oThisClass;
});
