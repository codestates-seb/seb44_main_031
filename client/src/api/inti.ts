export default {
  async getFourItem() {
    const response = await fetch(
      'http://cozshopping.codestates-seb.link/api/v1/products?count=4'
    );
    const data = await response.json();
    return data;
  },

  async getAllItem() {
    const response = await fetch(
      'http://cozshopping.codestates-seb.link/api/v1/products'
    );
    const data = await response.json();
    return data;
  },
};
