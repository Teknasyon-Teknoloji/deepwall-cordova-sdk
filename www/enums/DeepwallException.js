/**
 * Custom Exception
 */
exports.DeepwallException = class extends Error {
  constructor(error, meta) {
    super(error.message);

    this.code = error.code;

    if (meta) {
      this.meta = meta;
    }
  }
}
