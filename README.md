Nearby Shops 
[![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=Nearby%20Shops%20Open%20Source%20Food%20Delivery%20and%20Hyperlocal%20app&url=https://github.com/NearbyShops/Nearby-Shops-End-User-Android-app&via=moraysumeet&hashtags=opensource,androiddev,fooddelivery,android,ecommerce)
=============

<img src="https://nearbyshops.org/images/Roundlogos.png" width="80">  <a href="https://play.google.com/store/apps/details?id=org.nearbyshops.whitelabelapp"><img class="alignnone" src="https://goldtonemusicgroup.com/img/goldtone/main-page/news/playstore-badge.png" alt="Get it on Google Play" width="130" height="40" /></a> <a href="https://twitter.com/nearbyshopsapp?ref_src=twsrc%5Etfw" class="twitter-follow-button" data-show-count="false">
<img src="https://www.mathlearningcenter.org/sites/default/files/images/Follow%20on%20Twitter.png"
width="120">
</a>



About Nearby Shops - MultiVendor Food Delivery and Ecommerce App
--------------------------------------------------------------------

Nearby Shops is a **Free and Open-Source** food delivery, grocery and local shopping platform. 

Install using docker in 10 minutes at http://developer.nearbyshops.org

Website :  https://nearbyshops.org | Developers Guide: http://developer.nearbyshops.org


<img src="https://nearbyshops.org/images/1.jpg" width="208">   <img src="https://nearbyshops.org/images/3.jpg" width="208">   <img src="https://nearbyshops.org/images/6.jpg" width="208"> 



## 🚩 Table of Contents
- [Tech Stack and App Architecture](#tech-stack-and-app-architecture)
- [Features and highlightList](#features-and-highlightList)
- [Libraries Used](#libraries-used)
- [Third Party Integrations](#third-party-integrations)
- [Concept](#concept)
- [Connect with Us](#community---need-help-)
- [Contributions Required](#contributions-welcome)
- [Development Instructions](#development-instructions)
- [License](#license)





Tech Stack and app Architecture
--------------------------------

Uses Android Jetpack and Google’s Recommended Architecture Patterns

Written in both Java and Kotlin

Libraries Used Retrofit, Ok-HTTP, Picasso, Dagger, and the Mapbox Android SDK

User-Interface is made using the modular View-Holder Pattern which makes the UI blocks modular, easy to reuse, modify and understand.

The app uses Single Activity Architecture, where most of the screens are implemented as fragments and there are only 2-3 activities in the entire project.



Features and Highlights
----------------------------

Support for Multiple Currencies : You can select your country and currency for your local market will be set accordingly.

Mobile First - Multi-Vendor Platform where customers can send orders to multiple Shops / Restaurants

Integrations for Sending SMS-OTP, E-mail and Push Notifications are available

Order Tracking, with live status updates for customers using E-mail, SMS and Push Notifications

Rating and Reviews for items and shops

Home delivery and pick-up from the shop (Online-to-Offline) Shopping Concept is Supported

Location based filtering is available, which means that customers will see only those shops which can deliver to their address -- other shops will be filtered out. 

Vendors / Shop-Owners can track orders through orders inventory and update the order progress !

By adding delivery staff, shop-owners can deliver orders by themselves. Delivery by Market is also available. 

Billing and payments are supported. Shop-owners are billed according to the number of orders they deliver successfully. And Service Providers (Market Owners) can collect payments from the shop-owners. 


Order Delivery
----------------------------
We support both "Delivery by Vendors" and "Delivery by Market" delivery options. Vendors can provide delivery using their own delivery staff. Delivery can also be provided by Delivery Staff Employed by the Market Administrator.


Libraries Used
---------------

Android-Jetpack, Retrofit, Ok-Http, Event-Bus, Picasso, butterknife, dagger2, U-Crop, Gesture Views, Smiley Rating, Mapbox Android SDK


Third Party Integrations
-------------------------

SMS-OTP using MSG91

E-mail using Simple-Java-Mail E-mail Library

Push Notifications using Firebase FCM

Maps using Mapbox Android SDK and Google Maps

Payment Gateway - Razorpay

For more information please see https://developer.nearbyshops.org/integrations/e-mail-integration.html


Concept
----------
Nearby Shops is a Hyperlocal Shopping platform where a customer can buy directly from the shops available in his/her local area and get their orders delivered to their home or just pick the order up from the shop.

In the new emerging world, customers are getting tired of going outside the home to shop every day. They would much prefer to simply place an order and have the products delivered to their home the same day.

Conventional e-commerce has issues ... the delivery takes a long time and it's more difficult to trust unknown vendors whom you cannot see and meet. Hyperlocal e-commerce solves these issues because delivery is faster and customers can reach out to the vendors easily. 

Nearby Shops can also be used as a food Delivery platform where restaurants can use it to deliver food. 

Nearby Shops implements Alibaba's New Offline-to-Online Concept, where customers discover products online and then pick those products up from physical stores. 

![Online to Offline](https://www.innovationiseverywhere.com/wp-content/uploads/2015/05/what-is-o2o-online-to-offline-digital-ecommerce-retail-china-gmic-1-2.jpg)



Connect With us - Need Help ? 
------------------------

If you want any help regarding anything. Feel free to contact us -- send a message through our forum or you can simply post an issue. Your issues will not be ignored and you will surely receive help. 

Discourse Forum - https://forum.nearbyshops.org

Follow us Twitter - https://twitter.com/nearbyshopsapp

Facebook - https://www.facebook.com/nearbyshops

Facebook Group - https://www.facebook.com/groups/1144257952430940/



Contributions Welcome
-----------------------------------------------

https://forum.nearbyshops.org/t/project-roadmap-and-contributions-required/34/4

Improved Search with Search Suggestions for android app

Develop a PWA and Web app for Nearby Shops

i18n Internationalization support

Payment Gateway Integrations

Performance Testing and Optimization

Suggestions and Improvements in App Architecture

Code Review and Architecture



License
=======

Copyright (c) 2020 Nearby Shops

                     GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.

                            Preamble

  The GNU General Public License is a free, copyleft license for
software and other kinds of works.

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
