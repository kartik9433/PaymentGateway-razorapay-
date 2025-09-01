async function payNow(e) {
  e.preventDefault(); 

  const name = document.getElementById('name').value;
  const email = document.getElementById('email').value;
  const phone = document.getElementById('phone').value;
  const course = document.getElementById('course').value;

  let amount = 0;
  if(course === "Java") amount = 5000;
  else if(course === "DataScience") amount = 7000;
  else if(course === "MachineLearning") amount = 10000;
  else { alert("Please select a course"); return; }

  const orderData = {
    name: name,
    email: email,
    phone: phone,
    courseName: course,
    amount: amount
  };

  try {
    const response = await fetch('http://localhost:8080/api/payment/create-order', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(orderData)
    });

    const order = await response.json(); 

    const options = {
      key: "${RAZORPAY_KEY_ID}",   
      amount: amount * 100,       
      currency: "INR",
      name: "Course Payment",
      description: `Payment for ${course} course`,
      order_id: order.id,           
      handler: async function(paymentResponse) {
        await fetch(`http://localhost:8080/api/payment/update-order?paymentId=${paymentResponse.razorpay_payment_id}&orderId=${order.id}&status=SUCCESS`, {
          method: 'POST'
        });
        alert("Payment Successful! Email sent.");
        document.getElementById('paymentForm').reset();
      },
      prefill: {
        name: name,
        email: email,
        contact: phone
      },
      theme: { color: "#4CAF50" }
    };

    const rzp = new Razorpay(options);
    rzp.open();

  } catch (error) {
    console.error(error);
    alert("Something went wrong! Check console for details.");
  }
}
