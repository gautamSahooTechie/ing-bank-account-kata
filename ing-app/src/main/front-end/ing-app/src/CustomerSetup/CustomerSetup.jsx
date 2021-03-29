import React, { PureComponent } from 'react';

class CustomerSetup extends PureComponent { 
  constructor(props) {
    super(props);

    this.state = {
      hasError: false,
      customerCif: "",
      customerName: "",
      dateOfBirth: ""
    };
  }

  componentWillMount = () => {
    console.log('CustomerSetup will mount');
  }

  componentDidMount = () => {
    console.log('CustomerSetup mounted');
  }

  componentWillReceiveProps = (nextProps) => {
    console.log('CustomerSetup will receive props', nextProps);
  }

  componentWillUpdate = (nextProps, nextState) => {
    console.log('CustomerSetup will update', nextProps, nextState);
  }

  componentDidUpdate = () => {
    console.log('CustomerSetup did update');
  }

  componentWillUnmount = () => {
    console.log('CustomerSetup will unmount');
  }

  handleInputChange = (event) => {
    const target = event.target;
    const value = target.type === 'checkbox' ? target.checked : target.value;
    const name = target.name;

    this.setState({
      [name]: value
    });
  }

  postToServer = () => {
    fetch('http://localhost:8080/customer/createCustomer', {
          method: 'POST',
          mode: "no-cors",
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
          }, 
          body: JSON.stringify({
            customerCif: this.state.customerCif,
            customerName: this.state.customerName,
            dateOfBirth: this.state.dateOfBirth})
    });
    
 }

  render () {
    if (this.state.hasError) {
      return <h1>Something went wrong.</h1>;
    }
    
    return (
      <div className="CustomerSetupWrapper">
      <form name="customer-setup" method="post">
      <label>Customer Cif : </label>
      <input name="customerCif" id="customerCif" value={this.state.customerCif} onChange = {this.handleInputChange} type="text"/>
      <label>Customer Name : </label>
      <input name="customerName" id="customerName" value={this.state.customerName} onChange = {this.handleInputChange} type="text"/>
      <label>Customer DOB : </label>
      <input name="dateOfBirth" id="dateOfBirth" value={this.state.dateOfBirth} onChange = {this.handleInputChange} type="date"/>
      <br></br>
      <br></br>
      <button type="submit" onClick={this.postToServer}>Save</button>
      </form>
      </div>
    );
  }
}

CustomerSetup.propTypes = {
  // bla: PropTypes.string,
};

CustomerSetup.defaultProps = {
  // bla: 'test',
};

export default CustomerSetup;
