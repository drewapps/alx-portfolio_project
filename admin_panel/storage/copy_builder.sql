-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 21, 2023 at 01:40 PM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 8.0.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `copy_builder`
--

-- --------------------------------------------------------

--
-- Table structure for table `ads_setting`
--

CREATE TABLE `ads_setting` (
  `id` int(11) NOT NULL,
  `key_name` varchar(255) DEFAULT NULL,
  `key_value` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `ads_setting`
--

INSERT INTO `ads_setting` (`id`, `key_name`, `key_value`, `created_at`, `updated_at`) VALUES
(1, 'admob_publisher_id', NULL, '2023-04-14 12:37:57', '2023-04-20 09:59:50'),
(2, 'admob_app_id', NULL, '2023-04-14 12:38:30', '2023-04-20 09:59:50'),
(3, 'rewarded_id', NULL, '2023-04-14 12:38:44', '2023-04-20 09:59:50'),
(4, 'rewarded_display_type', 'FALSE', '2023-04-14 12:38:56', '2023-04-20 09:59:50'),
(5, 'banner_id', NULL, '2023-04-14 12:39:13', '2023-04-20 09:59:50'),
(6, 'banner_display_type', 'FALSE', '2023-04-14 12:39:27', '2023-04-20 09:59:50'),
(7, 'interstitial_id', NULL, '2023-04-14 12:39:41', '2023-04-20 09:59:50'),
(8, 'interstitial_display_type', 'FALSE', '2023-04-14 12:39:55', '2023-04-20 09:59:50'),
(9, 'click_interstitial_ad', '2', '2023-04-14 12:40:07', '2023-04-20 09:59:50'),
(10, 'native_id', NULL, '2023-04-14 12:40:19', '2023-04-20 09:59:50'),
(11, 'native_display_type', 'FALSE', '2023-04-14 12:41:37', '2023-04-20 09:59:50'),
(12, 'daily_limit_rewarded', '5', '2023-04-20 05:09:24', '2023-04-20 09:59:50'),
(13, 'rewarded_word', '0', '2023-04-20 05:09:38', '2023-04-20 09:59:50'),
(14, 'rewarded_image', '0', '2023-04-20 05:09:49', '2023-04-20 09:59:50'),
(15, 'app_opens_ads_enable', '0', '2023-04-20 05:10:10', '2023-04-20 09:59:50'),
(16, 'app_open_ads_id', NULL, '2023-04-20 05:10:22', '2023-04-20 09:59:50');

-- --------------------------------------------------------

--
-- Table structure for table `ai_image`
--

CREATE TABLE `ai_image` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `resolution` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `app_update_setting`
--

CREATE TABLE `app_update_setting` (
  `id` int(11) NOT NULL,
  `key_name` varchar(255) DEFAULT NULL,
  `key_value` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `app_update_setting`
--

INSERT INTO `app_update_setting` (`id`, `key_name`, `key_value`, `created_at`, `updated_at`, `deleted_at`) VALUES
(1, 'update_popup_show', '1', '2022-07-26 03:42:27', '2023-03-21 07:15:12', NULL),
(2, 'new_app_version_code', '1.0', '2022-07-26 03:42:27', '2023-03-21 07:15:12', NULL),
(3, 'description', 'Our free mobile-friendly tool offers a variety of randomly generated keys and passwords you can use to secure any application, service or device. Simply click to copy a password or press the \'Generate\' button for an entirely new set.', '2022-07-26 03:42:27', '2023-03-21 07:15:12', NULL),
(4, 'app_link', 'https://play.google.com/store/apps/', '2022-07-26 03:42:27', '2023-03-21 07:15:12', NULL),
(5, 'cancel_option', '0', '2022-07-26 03:42:27', '2023-03-21 07:15:12', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `blog`
--

CREATE TABLE `blog` (
  `id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `slug` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `meta_description` text DEFAULT NULL,
  `meta_keyword` text DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `blog_category`
--

CREATE TABLE `blog_category` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `coupon_code`
--

CREATE TABLE `coupon_code` (
  `id` int(11) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `discount` varchar(255) DEFAULT NULL,
  `limit` int(11) DEFAULT NULL,
  `subscription_id` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `valid_until` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `coupon_code_store`
--

CREATE TABLE `coupon_code_store` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `documents`
--

CREATE TABLE `documents` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `text` text DEFAULT NULL,
  `templates_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `document_word` bigint(20) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `email_setting`
--

CREATE TABLE `email_setting` (
  `id` int(11) NOT NULL,
  `key_name` varchar(255) DEFAULT NULL,
  `key_value` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `email_setting`
--

INSERT INTO `email_setting` (`id`, `key_name`, `key_value`, `created_at`, `updated_at`, `deleted_at`) VALUES
(1, 'smtp_host', 'smtp.mailtrap.io', '2022-07-27 04:18:10', '2023-02-26 09:14:13', NULL),
(2, 'username', '9e2ac0ac04788f', '2022-07-27 04:18:10', '2023-02-26 09:14:13', NULL),
(3, 'password', 'e4f6fef9cb1512', '2022-07-27 04:18:10', '2022-08-08 04:36:20', NULL),
(4, 'encryption', 'tls', '2022-07-27 04:18:10', '2023-02-26 09:14:13', NULL),
(5, 'port', '2525', '2022-07-27 04:18:10', '2023-02-26 09:14:13', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `email_verify`
--

CREATE TABLE `email_verify` (
  `user_id` int(11) DEFAULT NULL,
  `code` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `failed_jobs`
--

CREATE TABLE `failed_jobs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `uuid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `connection` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `queue` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `payload` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `exception` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `failed_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `favorite_template`
--

CREATE TABLE `favorite_template` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `template_id` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `inquiry`
--

CREATE TABLE `inquiry` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `message` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `invoice`
--

CREATE TABLE `invoice` (
  `id` int(11) NOT NULL,
  `payment_id` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `subscription_id` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `invoice_setting`
--

CREATE TABLE `invoice_setting` (
  `id` int(11) NOT NULL,
  `key_name` varchar(255) DEFAULT NULL,
  `key_value` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `invoice_setting`
--

INSERT INTO `invoice_setting` (`id`, `key_name`, `key_value`, `created_at`, `updated_at`) VALUES
(1, 'invoice_logo', 'e09c440b-edf6-4dfe-b750-df9641596d31.png', '2022-12-19 05:43:30', '2023-02-26 09:18:15'),
(2, 'invoice_prefix', 'INV', '2022-12-19 05:43:59', '2023-02-26 09:18:15'),
(3, 'invoice_number_digit', '4', '2022-12-19 05:44:12', '2023-02-26 09:18:15'),
(4, 'terms_and_conditions', 'Thank You', '2022-12-19 06:12:12', '2023-02-26 09:18:15');

-- --------------------------------------------------------

--
-- Table structure for table `migrations`
--

CREATE TABLE `migrations` (
  `id` int(10) UNSIGNED NOT NULL,
  `migration` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `batch` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `migrations`
--

INSERT INTO `migrations` (`id`, `migration`, `batch`) VALUES
(1, '2014_10_12_000000_create_users_table', 1),
(2, '2014_10_12_100000_create_password_resets_table', 1),
(3, '2019_08_19_000000_create_failed_jobs_table', 1),
(4, '2019_12_14_000001_create_personal_access_tokens_table', 1);

-- --------------------------------------------------------

--
-- Table structure for table `openai_model`
--

CREATE TABLE `openai_model` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `openai_model`
--

INSERT INTO `openai_model` (`id`, `name`, `created_at`, `updated_at`) VALUES
(1, 'text-davinci-003', '2023-03-21 05:28:50', '2023-03-21 05:28:50');

-- --------------------------------------------------------

--
-- Table structure for table `other_setting`
--

CREATE TABLE `other_setting` (
  `id` int(11) NOT NULL,
  `key_name` varchar(255) DEFAULT NULL,
  `key_value` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `other_setting`
--

INSERT INTO `other_setting` (`id`, `key_name`, `key_value`, `created_at`, `updated_at`) VALUES
(1, 'privacy_policy', NULL, '2022-07-26 03:56:26', '2022-07-27 06:44:57'),
(2, 'refund_policy', NULL, '2022-07-26 03:56:32', '2022-07-26 04:09:59'),
(3, 'terms_condition', NULL, '2022-07-26 03:56:35', '2022-07-26 04:09:47'),
(4, 'about_us', NULL, '2023-03-01 13:54:06', '2023-03-01 13:59:53');

-- --------------------------------------------------------

--
-- Table structure for table `password_resets`
--

CREATE TABLE `password_resets` (
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `payment_setting`
--

CREATE TABLE `payment_setting` (
  `id` int(11) NOT NULL,
  `key_name` varchar(255) DEFAULT NULL,
  `key_value` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `payment_setting`
--

INSERT INTO `payment_setting` (`id`, `key_name`, `key_value`, `created_at`, `updated_at`) VALUES
(1, 'razorpay_key_id', NULL, '2022-07-26 02:00:23', '2023-04-21 09:20:09'),
(2, 'razorpay_key_secret', NULL, '2022-07-26 02:00:23', '2023-04-21 09:20:09'),
(3, 'razorpay_enable', '0', '2022-07-26 02:01:37', '2023-04-21 09:20:09'),
(4, 'stripe_publishable_Key', NULL, '2023-02-26 08:18:16', '2023-04-21 09:20:09'),
(5, 'stripe_secret_key', NULL, '2023-02-26 08:48:49', '2023-04-21 09:20:09'),
(6, 'stripe_enable', '0', '2023-02-26 08:49:57', '2023-04-21 09:20:09'),
(7, 'currency', 'USD', '2023-02-27 05:02:58', '2023-04-21 09:20:09');

-- --------------------------------------------------------

--
-- Table structure for table `personal_access_tokens`
--

CREATE TABLE `personal_access_tokens` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tokenable_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tokenable_id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `abilities` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_used_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `plan`
--

CREATE TABLE `plan` (
  `id` int(11) NOT NULL,
  `plan_name` varchar(255) NOT NULL,
  `plan_price` varchar(255) DEFAULT NULL,
  `total_words` bigint(20) DEFAULT NULL,
  `number_of_images` bigint(20) DEFAULT NULL,
  `rewarded_enable` int(11) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `duration_type` varchar(255) DEFAULT NULL,
  `google_product_enable` int(11) DEFAULT NULL,
  `google_product_id` text DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `most_popular` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `setting`
--

CREATE TABLE `setting` (
  `id` int(11) NOT NULL,
  `key_name` varchar(255) DEFAULT NULL,
  `key_value` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `setting`
--

INSERT INTO `setting` (`id`, `key_name`, `key_value`, `created_at`, `updated_at`) VALUES
(1, 'black_logo', 'eab98781-bdaa-4d26-9ef1-974fc58c0987.png', '2022-12-15 04:15:41', '2023-02-27 05:05:01'),
(2, 'white_logo', 'c56679c0-d48e-4e9e-8ec8-30ff0815be8e.png', '2022-12-15 04:15:58', '2023-02-28 05:44:49'),
(3, 'favicon', 'bf122b87-8c8d-43ff-b2b3-b04cd418e881.png', '2022-12-15 04:16:17', '2023-02-28 05:25:40'),
(4, 'email', 'copybuilder@gmail.com', '2022-12-15 04:16:30', '2023-04-05 05:16:46'),
(5, 'phone_no', '7485859685', '2022-12-15 04:16:44', '2023-04-05 05:16:46'),
(6, 'address', '11, Shiv Shilpa Shopp Centre, Vasant Vihar, Opp Lokpuvan, Thane', '2022-12-15 04:16:59', '2023-04-05 05:16:46'),
(7, 'description', 'In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content.', '2022-12-15 04:17:14', '2023-04-05 05:16:46'),
(8, 'facebook', 'https://www.facebook.com/copy-builder', '2022-12-15 04:31:55', '2023-04-05 05:16:46'),
(9, 'instagram', 'https://www.instagram.com/copy-builder', '2022-12-15 04:32:11', '2023-04-05 05:16:46'),
(10, 'youtube', 'https://www.youtube.com/channel/UC7O6XcVZCRF', '2022-12-15 04:32:26', '2023-04-05 05:16:46'),
(11, 'header_script', '<meta name=\"keywords\" content=\"\">', '2022-12-22 04:16:17', '2023-04-05 05:16:46'),
(12, 'app_name', 'Copy Builder', '2023-01-02 09:19:47', '2023-04-05 05:16:46'),
(13, 'openai_key', NULL, '2023-02-26 12:58:07', '2023-04-19 05:14:12'),
(14, 'footer_text', '<strong>© 2023 Power by CopyBuilder</strong>', '2023-02-27 10:27:12', '2023-04-05 05:16:46'),
(15, 'linkedin', 'https://www.linkedin.com/copy-builder', '2023-02-28 10:03:14', '2023-04-05 05:16:46'),
(16, 'twitter', 'https://twitter.com/copy-builder', '2023-02-28 10:03:26', '2023-04-05 05:16:46'),
(17, 'licence_active', '0', '2023-03-02 09:38:25', '2023-04-10 10:21:21'),
(18, 'register_user_default_word', '1000', '2023-03-11 06:02:29', '2023-04-05 05:16:46'),
(19, 'register_user_default_image', '10', '2023-03-11 06:03:59', '2023-04-05 05:16:46'),
(20, 'api_key', '123456', '2023-03-11 06:13:36', '2023-04-05 05:16:46'),
(21, 'openai_model', 'text-davinci-003', '2023-03-21 05:09:21', '2023-04-19 05:14:12');

-- --------------------------------------------------------

--
-- Table structure for table `social_login_setting`
--

CREATE TABLE `social_login_setting` (
  `id` int(11) NOT NULL,
  `key_name` varchar(255) DEFAULT NULL,
  `key_value` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `social_login_setting`
--

INSERT INTO `social_login_setting` (`id`, `key_name`, `key_value`, `created_at`, `updated_at`) VALUES
(1, 'google_enable', '1', '2023-02-26 13:14:46', '2023-02-26 13:42:29'),
(2, 'google_api_key', NULL, '2023-02-26 13:15:09', '2023-02-26 13:42:29'),
(3, 'google_api_secret', NULL, '2023-02-26 13:15:21', '2023-02-26 13:42:29'),
(4, 'google_redirect', 'YOUR_URL/auth/callback/google', '2023-02-26 13:15:34', '2023-02-26 13:42:29');

-- --------------------------------------------------------

--
-- Table structure for table `subscription_plan`
--

CREATE TABLE `subscription_plan` (
  `id` int(11) NOT NULL,
  `plan_name` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `duration_type` varchar(255) DEFAULT NULL,
  `plan_price` float(10,2) DEFAULT NULL,
  `discount_price` float(10,2) DEFAULT NULL,
  `total_words` bigint(20) DEFAULT NULL,
  `number_of_images` int(11) DEFAULT NULL,
  `plan_type` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT 1,
  `most_popular` int(11) NOT NULL DEFAULT 0,
  `include_plan_detail` text DEFAULT NULL,
  `not_include_plan_detail` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `support_messages`
--

CREATE TABLE `support_messages` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED DEFAULT NULL,
  `ticket_id` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `attachment` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `message` longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `support_ticket`
--

CREATE TABLE `support_ticket` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `priority` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `ticket_id` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `resolved_on` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `templates`
--

CREATE TABLE `templates` (
  `id` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `slug` varchar(255) DEFAULT NULL,
  `description` text NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `templates`
--

INSERT INTO `templates` (`id`, `title`, `slug`, `description`, `image`, `type`, `status`, `created_at`, `updated_at`) VALUES
(1, 'Linkedin Post', 'linkedin-post', 'Write Inspiring Linkedin posts that will help you build trust and authority in your industry.', 'linkedin.png', 'Social', 1, '2023-02-17 12:04:54', '2023-02-26 13:01:13'),
(2, 'Instagram Caption', 'instagram-caption', 'Write a Catchy caption for your Instagram post.', 'instagram.png', 'Social', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(3, 'Twitter Tweets', 'twitter-tweets', 'Generate tweets using AI that are relevant and on-trend.', 'twitter.png', 'Social', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(4, 'Trending Instagram Hashtags', 'trending-instagram-hashtags', 'Generate the Most Related & Trending Hashtag for Instagram Post.', 'instagram_hash.png', 'Social', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(5, 'Youtube Video Description', 'youtube-video-description', 'Write Catchy and persuasive YouTube descriptions that help your videos rank higher.', 'youtube.png', 'Social', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(6, 'Youtube Video Ideas', 'youtube-video-ideas', 'Brainstorm new video topics that will engage viewers and rank well on YouTube.', 'youtube.png', 'Social', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(7, 'Youtube Video Outlines', 'youtube-video-outlines', 'YouTube Video outlines that are create and engaging.', 'youtube.png', 'Social', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(8, 'Youtube Video Title', 'youtube-video-title', 'Write a Catchy & engaging Video title for a higher ranking.', 'youtube.png', 'Social', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(9, 'Youtube Video Tag', 'youtube-video-tag', 'Generate the perfect tag for your Youtube Videos.', 'youtube.png', 'Social', 0, '2023-02-17 12:04:54', '2023-02-26 06:51:33'),
(10, 'TikTok Video Scripts', 'tiktok-video-scripts', 'Generate Video scripts that are ready to shoot and will make you go viral.TikTok.', 'tik-tok.png', 'Social', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(11, 'Youtube Video Scripts', 'youtube-video-scripts', 'Video scripts that are ready to shoot and will make you go viral on YouTube.', 'youtube.png', 'Social', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(12, 'Instagram Reel Scripts', 'instagram-reel-scripts', 'Reel scripts that are ready to shoot and will make you go viral on Instagram.', 'instagram.png', 'Social', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(13, 'Youtube Short Scripts', 'youtube-short-scripts', 'Short scripts that are ready to shoot and will make you go viral on Instagram.', 'youtube.png', 'Social', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(14, 'Facebook Ads', 'facebook-ads', 'Create high-converting Facebook ad copy that makes your ads stand out.', 'facebook.png', 'Ads & Marketing', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(15, 'Linkedin Ads Headline', 'linkedin-ads-headline', 'Attention-grabbing and high-converting ad headlines for Linkedin.', 'linkedin.png', 'Ads & Marketing', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(16, 'Linkedin Ads Description', 'linkedin-ads-description', 'Write Professional and eye-catching ad descriptions that will make your product shine.', 'linkedin.png', 'Ads & Marketing', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(17, 'Google Ads Titles', 'google-ads-titles', 'Write a high-converting ad headline for Google.', 'google_ads.png', 'Ads & Marketing', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(18, 'Google Ads Description', 'google-ads-description', 'Write Professional and eye-catching ad descriptions that will make your ad shine.', 'google_ads.png', 'Ads & Marketing', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(19, 'App & SMS Notification', 'app-sms-notification', 'Write Notification messages for your apps, websites, and mobile devices.', 'app_notification.png', 'Ads & Marketing', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(20, 'Product Description', 'product-description', 'Create compelling product descriptions to be used on websites and social media.', 'product.png', 'Ecommerce', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(21, 'AIDA Framework', 'aida-framework', 'Tried and tested the formula of Attention, Interest, Desire, and Action that is proven to convert.', 'aida.png', 'Ecommerce', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(22, 'Amazon Product Description (Paragraph)', 'amazon-product-description-paragraph', 'Create compelling product descriptions for Amazon listings.', 'amazon.png', 'Ecommerce', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(23, 'Amazon Product Feature (Bullet)', 'amazon-product-feature-bullet', 'Pors and features of your products that will make them irresistible to shoppers.', 'amazon.png', 'Ecommerce', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(24, 'Content Improver', 'content-improver', 'Take a piece of content and rewrite it to make it more interesting, creative, and engaging.', 'conent_improver.png', 'Ecommerce', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(25, 'SEO Title and Meta Description', 'seo-title-and-meta-description', 'Write SEO-optimized title tags and meta descriptions for the product page that will rank well on Google.', 'seo.png', 'Ecommerce', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(26, 'Amazon Product Title', 'amazon-product-title', 'Write a high-converting and engaging title for the product.', 'amazon.png', 'Ecommerce', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(27, 'Amazon Sponsered Brand Ad Headline', 'amazon-sponsered-brand-ad-headline', 'Awesome Amazon ad headlines that will increase your conversion rate.', 'amazon.png', 'Ecommerce', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(28, 'Flipkart Product Title', 'flipkart-product-title', 'Write a high-converting and engaging title for the product.', 'flipkart-icon.png', 'Ecommerce', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(29, 'Flipkart Product Description', 'flipkart-product-description', 'Create compelling product descriptions for Flipkart listings.', 'flipkart-icon.png', 'Ecommerce', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(30, 'Article Writer', 'article-writer', 'Write a Premium Blog Article with SEO Elements & easily Ranked in Google. ', 'article_writing.png', 'Article & Blog', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(31, 'Blog Post Topic Ideas', 'blog-post-topic-ideas', 'Brainstorm new blog post topics that will engage readers and rank well on Google.', 'blog_post_idea.png', 'Article & Blog', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(32, 'Blog Post Outlines', 'blog-post-outlines', 'Detailed Article outlines that help you write better content consistently.', 'blog_post_outline.png', 'Article & Blog', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(33, 'Blog Post Intro Paragraph', 'blog-post-intro-paragraph', 'Write Your Amazing Opening Paragraph for your Article.', 'blog_post_paragraph.png', 'Article & Blog', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(34, 'Blog Post Conclusion Paragraph', 'blog-post-conclusion-paragraph', 'Write Your Amazing Conclusion Paragraph for your Article. ', 'blog_post_paragraph.png', 'Article & Blog', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(35, 'FAQ Generator', 'faq-generator', 'Finish your blog post with some FAQs about your topic.', 'faq.png', 'Article & Blog', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(36, 'Content Rewriter', 'content-rewriter', 'You can efficiently Rewrite content or article within a minute.', 'content_rewriting.png', 'Article & Blog', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(37, 'Content Rephrase', 'content-rephrase', 'You can efficiently Rephrase content or article within a minute.', 'content_rephrase.png', 'Article & Blog', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(38, 'Text Summarizer', 'text-summarizer', 'Shortened text copy that provides the main ideas and most important details of your original text.', 'text_summerize.png', 'Article & Blog', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(39, 'Paragraph Generator', 'paragraph-generator', 'Write paragraphs that will captivate your readers.', 'paragraph_generator.png', 'Article & Blog', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(40, 'Text Expander', 'text-expander', 'Expand a short sentence or a few words into multiple sentences.', 'text_extender.png', 'General Writing', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(41, 'Company Bio', 'company-bio', 'Write a Short and sweet company bio that will help you connect with your target audience.', 'company_bio.png', 'General Writing', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(42, 'Quora Answers', 'quora-answers', 'Write an Intelligent answer for Quora Questions.', 'quora.png', 'General Writing', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(43, 'Bullet Point Answers', 'bullet-point-answers', 'Informative bullet points that provide quick and valuable answers to your customers\' questions.', 'bullet_point.png', 'General Writing', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(44, 'Answers', 'answers', 'Instant, quality answers to any questions for your concern.', 'answer.png', 'General Writing', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(45, 'Pros and Cons', 'pros-and-cons', 'List of the main benefits versus the most common problems and concerns.', 'pro_and_cons.png', 'General Writing', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(46, 'Generate Question', 'generate-question', 'Generate Engaging Questions for your Realtime Audience.', 'question.png', 'General Writing', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(47, 'Company Mission', 'company-mission', 'A clear and concise statement of your company\'s goals and purpose.', 'company_misson.png', 'General Writing', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(48, 'Company Vision', 'company-vision', 'A vision that attracts the right people, clients, and employees.', 'company_vision.png', 'General Writing', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(49, 'PlayStore App Title', 'playstore-app-title', 'Write a Perfect ASO Title for Apps/Games.', 'google-play.png', 'ASO', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(50, 'PlayStore App Short Description', 'playstore-app-short-description', 'Generate an ASO Friendly App & Game Short Description with Keywords.', 'google-play.png', 'ASO', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(51, 'PlayStore App Description', 'playstore-app-description', 'Write a High-Quality App/Game Long Description with the keyword Maintain.', 'google-play.png', 'ASO', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(52, 'AppStore Title', 'appstore-title', 'Write a Perfect ASO Title for Apps/Games.', 'app-store.png', 'ASO', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(53, 'AppStore Sub Title', 'appstore-sub-title', 'Generate an ASO Friendly App & Game Sub Title with Keywords. ', 'app-store.png', 'ASO', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(54, 'AppStore Promotional Text', 'appstore-promotional-text', 'Write an Effective & Engaging App Store Promotional Text for Apps & Games.', 'app-store.png', 'ASO', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(55, 'AppStore Description', 'appstore-description', 'Write a High-Quality App/Game Long Description with the keyword Maintain.', 'app-store.png', 'ASO', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(56, 'App Reviews', 'app-reviews', 'Generate User-Friendly Reviews & Reply for your Games & Apps.', 'review.png', 'ASO', 1, '2023-02-17 12:04:54', '2023-02-17 12:05:55'),
(57, 'Pin Title & Description', 'pin-title-description', 'Create great Pinterest pins and descriptions that drive engagement, traffic, and reach.\n', 'pinterest.png', 'Social', 1, '2023-02-17 12:31:04', '2023-02-17 12:31:04'),
(58, 'Product Names', 'product-names', 'Write Catchy and meaningful names that fit your product or service.', 'product_name.png', 'Ecommerce', 1, '2023-02-17 12:32:28', '2023-02-17 12:32:28'),
(59, 'Landing Page Headlines', 'landing-page-headlines', ' Unique and catchy headlines that are perfect for your product or service.', 'landing-page.png', 'Article & Blog', 1, '2023-02-17 12:33:54', '2023-02-17 12:33:54'),
(60, 'SEO Meta Tags (Blog Post)', 'seo-meta-tags-blog-post', 'A set of optimized meta titles and meta description tags that will boost your search rankings for your blog.', 'meta_tag.png', 'Article & Blog', 1, '2023-02-17 12:35:53', '2023-02-17 12:35:53'),
(61, 'SEO Meta Tags (Product Page)', 'seo-meta-tags-product-page', 'A set of optimized meta titles and meta description tags that will boost your search rankings for your product page.', 'product tag.png', 'Article & Blog', 1, '2023-02-17 12:37:46', '2023-02-17 12:37:46'),
(62, 'Fix the Grammar', 'fix-the-grammar', 'Correct Sentences in Standard English. ', 'english.png', 'General Writing', 1, '2023-02-17 12:38:40', '2023-02-17 12:38:40'),
(63, 'Synonyms Generator', 'synonyms-generator', 'Create synonyms and Descriptions for any word.', 'synonyms.png', 'General Writing', 1, '2023-02-17 12:39:33', '2023-02-17 12:39:33'),
(64, 'Antonyms Generator', 'antonyms-generator', 'Create Antonyms and Descriptions for any term.', 'antonyms.png', 'General Writing', 1, '2023-02-17 12:40:08', '2023-02-17 12:40:08'),
(65, 'Sentence Simplifier', 'sentence-simplifier', 'Rephrase a sentence to have fewer words.\n', 'simplicity.png', 'General Writing', 1, '2023-02-17 12:40:58', '2023-02-17 12:40:58'),
(66, 'Freelance Project Proposal', 'freelance-project-proposal', 'Create great freelance projects proposal to help you bid on projects and win clients.\n', 'freelance-work.png', 'General Writing', 1, '2023-02-17 12:41:36', '2023-02-17 12:41:36'),
(67, 'Cover Letter', 'cover-letter', ' Generate, Brainstorm, and find the right words for a cover letter.', 'letter.png', 'General Writing', 1, '2023-02-17 12:42:52', '2023-02-17 12:42:52'),
(68, 'Call To Action', 'call-to-action', ' Get more website visitors to take action on your site.', 'action.png', 'General Writing', 1, '2023-02-17 12:43:37', '2023-02-17 12:43:37'),
(69, 'Checklist Ad Copy', 'checklist-ad-copy', 'Create a checklist for the ad copy for your business.', 'checklist.png', 'General Writing', 1, '2023-02-17 12:44:21', '2023-02-17 12:44:21'),
(70, 'Listicle Ideas', 'listicle-ideas', 'Creative listicle ideas that are easy to write and perform well on social media.\n', 'listicle_idea.png', 'General Writing', 1, '2023-02-17 12:45:22', '2023-02-17 12:45:22'),
(71, 'Startup Ideas', 'startup-ideas', ' Great startup ideas that you can get started on right away.', 'start-up.png', 'General Writing', 1, '2023-02-17 12:46:26', '2023-02-17 12:46:26'),
(72, 'Definition', 'definition', ' A definition for a word, phrase, or acronym that\'s used often by your target buyers.', 'definition.png', 'General Writing', 1, '2023-02-17 13:08:03', '2023-02-17 13:08:03'),
(73, 'Social Media Content Plan', 'social-media-content-plan', 'A social media content plan that will help you create engaging content for your audience.', 'social_media_content_plan.png', 'Social', 1, '2023-02-21 04:36:46', '2023-02-21 04:36:46'),
(74, 'Write Cold Emails', 'write-cold-emails', 'Reach out to potential leads with a well-crafted email introducing your company.', 'cold_email.png', 'Email', 1, '2023-02-21 04:38:08', '2023-02-21 04:38:08'),
(75, 'Cancellation Email', 'cancellation-email', 'Write emails to re-engage customers and bring them back to your business.', 'Cancellation Email.png', 'Email', 1, '2023-02-21 04:39:13', '2023-02-21 04:39:13'),
(76, 'Welcome Emails', 'welcome-emails', 'Generate warm, welcoming emails for new subscribers, customers, or users.', 'welcome_email.png', 'Email', 1, '2023-02-21 04:40:09', '2023-02-21 04:40:09'),
(77, 'Confirmation Emails', 'confirmation-emails', 'Write emails to acknowledge and follow-up on specific actions or events.', 'verification_email.png', 'Email', 1, '2023-02-21 04:41:11', '2023-02-21 04:41:11'),
(78, 'Email Subject Lines', 'email-subject-lines', 'Generate catchy, concise subject lines to grab the attention of your audience.', 'subject_line.png', 'Email', 1, '2023-02-21 04:42:20', '2023-02-26 07:20:49'),
(79, 'AIDA Copywriting Frame', 'aida-copywriting-frame', 'This Attention-Interest-Desire-Action Copywriting framework will help you boost your website conversions.', '1_AIDA Copywriting Frame.png', 'Copywriting', 1, '2023-03-16 11:06:32', '2023-03-16 11:06:32'),
(80, 'PAS Copywriting Frame', 'pas-copywriting-frame', 'Problem-Agitate-Solution Copywriting frame will help you sell any service or product more efficiently.', '2_PAS Copywriting Frame.png', 'Copywriting', 1, '2023-03-16 11:08:18', '2023-03-16 11:08:18'),
(81, 'Marketing USP', 'marketing-usp', 'Generate a Unique-Selling-Point (USP) text for your product or business based on a simple description.', '4_Marketing USP.png', 'Copywriting', 1, '2023-03-16 11:10:15', '2023-03-16 11:10:15'),
(82, 'BAB Copywriting Framework', 'bab-copywriting-framework', 'This Before - After - Bridge Copywriting framework will help you boost your website conversions.', '5_BAB Copywriting Framework.png', 'Copywriting', 1, '2023-03-16 11:11:35', '2023-03-16 11:11:35'),
(83, 'FAB Copywriting Framework', 'fab-copywriting-framework', 'This Features - Advantages - Benefits Copywriting framework will help you boost your website conversions.', '6_FAB Copywriting Framework.png', 'Copywriting', 1, '2023-03-16 11:12:43', '2023-03-16 11:12:43'),
(84, 'The 4 C Copywriting Framework', 'the-4-c-copywriting-framework', 'The 4 C\'s (Clear - Concise - Compelling - Credible) Copywriting framework will help you boost your website conversions.', '7_The 4 C Copywriting Framework.png', 'Copywriting', 1, '2023-03-16 11:13:43', '2023-03-16 11:13:43'),
(85, 'FOREST Copywriting Framework', 'forest-copywriting-framework', 'This Copywriting framework will help you boost your website conversions.', '8_FOREST Copywriting Framework.png', 'Copywriting', 1, '2023-03-16 11:14:55', '2023-03-16 11:14:55'),
(86, '5 Basic Objections Copywriting Framework', '5-basic-objections-copywriting-framework', 'This 5 Basic Objections Copywriting framework will help you boost your website conversions.', '9_5 Basic Objections Copywriting Framework.png', 'Copywriting', 1, '2023-03-16 11:16:02', '2023-03-16 11:16:02'),
(87, 'The PPPP Copywriting Framework', 'the-pppp-copywriting-framework', 'This PPPP Copywriting framework will help you boost your website conversions.', '10_The PPPP Copywriting Framework.png', 'Copywriting', 1, '2023-03-16 11:17:12', '2023-03-16 11:17:12'),
(88, 'The SCH Copywriting Framework', 'the-sch-copywriting-framework', 'This SCH Copywriting framework will help you boost your website conversions.', '11_The SCH Copywriting Framework.png', 'Copywriting', 1, '2023-03-16 11:18:24', '2023-03-16 11:18:24'),
(89, 'The SSS Copywriting Framework', 'the-sss-copywriting-framework', 'This SSS Copywriting framework will help you boost your website conversions.', '12_The SSS Copywriting Framework.png', 'Copywriting', 1, '2023-03-16 11:19:29', '2023-03-16 11:19:29'),
(90, 'PASTOR Copywriting Framework', 'pastor-copywriting-framework', 'This PASTOR Copywriting framework will help you boost your website conversions.', '13_PASTOR Copywriting Framework.png', 'Copywriting', 1, '2023-03-16 11:20:39', '2023-03-16 11:20:39'),
(91, 'ACCA Copywriting Framework', 'acca-copywriting-framework', 'This ACCA Copywriting framework will help you boost your website conversions.', '14_ACCA Copywriting Framework.png', 'Copywriting', 1, '2023-03-16 11:21:50', '2023-03-16 11:21:50'),
(92, '1-2-3-4 Copywriting Framework', '1-2-3-4-copywriting-framework', 'The 1–2–3–4 Formula Copywriting framework will help you boost your website conversions.', '15_1-2-3-4 Copywriting Framework.png', 'Copywriting', 1, '2023-03-16 11:22:46', '2023-03-16 11:22:46'),
(93, '6+1 Copywriting Framework', '6+1-copywriting-framework', 'This 6+1 (Model) Copywriting framework will help you boost your website conversions.', '16_6+1 Copywriting Framework.png', 'Copywriting', 1, '2023-03-16 11:44:42', '2023-03-16 11:44:42'),
(94, 'SLAP Copywriting Framework', 'slap-copywriting-framework', 'This SLAP Copywriting framework will help you boost your product sales.', '17_SLAP Copywriting Framework.png', 'Copywriting', 1, '2023-03-16 11:48:45', '2023-03-16 11:48:45'),
(95, '4U Copywriting Framework', '4u-copywriting-framework', 'The 4U Copywriting framework will help you boost your website conversions.', '18_4U Copywriting Framework.png', 'Copywriting', 1, '2023-03-16 11:49:42', '2023-03-16 11:49:42'),
(96, 'Pinterest Board Ideas', 'pinterest-board-ideas', 'Generate High Engaging board titles and descriptions for Any topic.', 'pinterest.png', 'Social', 1, '2023-04-21 06:54:42', '2023-04-21 06:54:42'),
(97, 'Pinterest Pin ideas', 'pinterest-pin-ideas', 'Generate High Engaging Pin titles and descriptions for Any topic.', 'pinterest.png', 'Social', 1, '2023-04-21 06:56:51', '2023-04-21 06:56:51'),
(98, 'Pinterest Ad Campaign', 'pinterest-ad-campaign', 'Generate ad creatives, targeting options, and a clear call-to-action.', 'pinterest.png', 'Ads & Marketing', 1, '2023-04-21 06:58:15', '2023-04-21 06:58:15'),
(99, 'Board Descriptions', 'board-descriptions', 'Write Pinterest SEO-friendly board descriptions in just 10 Second.', 'pinterest.png', 'Social', 1, '2023-04-21 06:59:45', '2023-04-21 06:59:45'),
(100, 'Pin Group Ideas', 'pin-group-ideas', 'Generate engaging group titles and descriptions for each!', 'pinterest.png', 'Social', 1, '2023-04-21 07:00:59', '2023-04-21 07:00:59'),
(101, 'Snapchat Content Ideas', 'snapchat-content-ideas', 'You can generate captivating captions and relevant hashtags.', 'snapchat.png', 'Social', 1, '2023-04-21 07:01:49', '2023-04-21 07:01:49'),
(102, 'Snapchat Story Series', 'snapchat-story-series', 'generate an Outline for a Snapchat Story series on any topic ideas!', 'snapchat.png', 'Social', 1, '2023-04-21 07:02:49', '2023-04-21 07:02:49'),
(103, 'Snapchat Spotlight Ideas', 'snapchat-spotlight-ideas', 'Generate captivating captions and relevant hashtags for Snapchat Spotlight', 'snapchat.png', 'Social', 1, '2023-04-21 07:03:56', '2023-04-21 07:03:56');

-- --------------------------------------------------------

--
-- Table structure for table `transaction`
--

CREATE TABLE `transaction` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `total_paid` varchar(255) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  `payment_id` varchar(255) DEFAULT NULL,
  `payment_type` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `subscription_id` int(11) DEFAULT NULL,
  `payment_receipt` varchar(255) DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mobile_no` bigint(20) DEFAULT NULL,
  `image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `oauth_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `oauth_type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_subscribe` int(11) DEFAULT NULL,
  `subscription_id` int(11) DEFAULT NULL,
  `subscription_start_date` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `subscription_end_date` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `words_left` bigint(20) DEFAULT NULL,
  `image_left` int(11) DEFAULT NULL,
  `email_verified_at` timestamp NULL DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `remember_token` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT 0,
  `status` int(11) NOT NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `mobile_no`, `image`, `oauth_id`, `oauth_type`, `user_type`, `is_subscribe`, `subscription_id`, `subscription_start_date`, `subscription_end_date`, `words_left`, `image_left`, `email_verified_at`, `password`, `remember_token`, `is_deleted`, `status`, `created_at`, `updated_at`) VALUES
(1, 'Super Admin', 'sunil@gmail.com', 9685747485, 'ea2cb744-a99b-47d4-a05f-23de6b8e3f97.png', NULL, NULL, 'Super Admin', NULL, NULL, NULL, NULL, NULL, NULL, '2023-03-02 13:29:09', '$2y$10$I8zwx6hxpepvRAIMA346Q.wDq9nWUfbbEKljax9HKKoptOp68gHy6', 'TpiIVioGyToZAk0qcYRLvbyD4uz3Pgkx2itngbPVsyshJWOCSaaG5BAjVsF2', 0, 1, '2023-02-07 10:13:40', '2023-03-22 05:29:03'),
(2, 'Demo', 'demo2023@gmail.com', 9685748596, 'a1552649-2fa4-41f1-a14a-b93cb8809582.png', NULL, NULL, 'Super Admin', NULL, NULL, NULL, NULL, NULL, NULL, '2023-03-02 13:29:09', '$2y$10$uev8.B2G.ml9eEiZRrHD2OYiA8FhBYJn0N2tpBSof13.m2Zzw1diK', 'pqH4NIKMpSQUB2as4bjFymwP6ykfh6XR3hKYzSWmZ86Bc6wrjfTC8AtGWrJy', 0, 1, '2023-02-07 11:13:40', '2023-03-02 10:14:06'),
(3, 'Demo', 'magik@demo.com', 1234567890, '68111807-e9ef-47f0-9cc7-b732c89f9540.png', NULL, NULL, 'Demo', NULL, NULL, NULL, NULL, 639, 8, '2023-03-02 13:29:09', '$2y$10$7eGVaevy/nQNjUD3xDf4jejdAKsM6IbX.VVmI01yD/oDPzoruUd16', NULL, 0, 1, '2023-03-02 13:29:09', '2023-04-21 08:17:33');

-- --------------------------------------------------------

--
-- Table structure for table `user_chat`
--

CREATE TABLE `user_chat` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `chat_id` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `text` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `user_delete_request`
--

CREATE TABLE `user_delete_request` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ads_setting`
--
ALTER TABLE `ads_setting`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ai_image`
--
ALTER TABLE `ai_image`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `app_update_setting`
--
ALTER TABLE `app_update_setting`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `blog`
--
ALTER TABLE `blog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `blog_category`
--
ALTER TABLE `blog_category`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `coupon_code`
--
ALTER TABLE `coupon_code`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `coupon_code_store`
--
ALTER TABLE `coupon_code_store`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `documents`
--
ALTER TABLE `documents`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `email_setting`
--
ALTER TABLE `email_setting`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `failed_jobs`
--
ALTER TABLE `failed_jobs`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `failed_jobs_uuid_unique` (`uuid`);

--
-- Indexes for table `favorite_template`
--
ALTER TABLE `favorite_template`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `inquiry`
--
ALTER TABLE `inquiry`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `invoice`
--
ALTER TABLE `invoice`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `invoice_setting`
--
ALTER TABLE `invoice_setting`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `migrations`
--
ALTER TABLE `migrations`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `openai_model`
--
ALTER TABLE `openai_model`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `other_setting`
--
ALTER TABLE `other_setting`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `password_resets`
--
ALTER TABLE `password_resets`
  ADD KEY `password_resets_email_index` (`email`);

--
-- Indexes for table `payment_setting`
--
ALTER TABLE `payment_setting`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `personal_access_tokens`
--
ALTER TABLE `personal_access_tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `personal_access_tokens_token_unique` (`token`),
  ADD KEY `personal_access_tokens_tokenable_type_tokenable_id_index` (`tokenable_type`,`tokenable_id`);

--
-- Indexes for table `plan`
--
ALTER TABLE `plan`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `setting`
--
ALTER TABLE `setting`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `social_login_setting`
--
ALTER TABLE `social_login_setting`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `subscription_plan`
--
ALTER TABLE `subscription_plan`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `support_messages`
--
ALTER TABLE `support_messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `support_messages_user_id_foreign` (`user_id`);

--
-- Indexes for table `support_ticket`
--
ALTER TABLE `support_ticket`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `templates`
--
ALTER TABLE `templates`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `transaction`
--
ALTER TABLE `transaction`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `users_email_unique` (`email`);

--
-- Indexes for table `user_chat`
--
ALTER TABLE `user_chat`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_delete_request`
--
ALTER TABLE `user_delete_request`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ads_setting`
--
ALTER TABLE `ads_setting`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `ai_image`
--
ALTER TABLE `ai_image`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

--
-- AUTO_INCREMENT for table `app_update_setting`
--
ALTER TABLE `app_update_setting`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `blog`
--
ALTER TABLE `blog`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `blog_category`
--
ALTER TABLE `blog_category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `coupon_code`
--
ALTER TABLE `coupon_code`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `coupon_code_store`
--
ALTER TABLE `coupon_code_store`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `documents`
--
ALTER TABLE `documents`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

--
-- AUTO_INCREMENT for table `email_setting`
--
ALTER TABLE `email_setting`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `failed_jobs`
--
ALTER TABLE `failed_jobs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `favorite_template`
--
ALTER TABLE `favorite_template`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `inquiry`
--
ALTER TABLE `inquiry`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `invoice`
--
ALTER TABLE `invoice`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `invoice_setting`
--
ALTER TABLE `invoice_setting`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `migrations`
--
ALTER TABLE `migrations`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `openai_model`
--
ALTER TABLE `openai_model`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `other_setting`
--
ALTER TABLE `other_setting`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `payment_setting`
--
ALTER TABLE `payment_setting`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `personal_access_tokens`
--
ALTER TABLE `personal_access_tokens`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `plan`
--
ALTER TABLE `plan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

--
-- AUTO_INCREMENT for table `setting`
--
ALTER TABLE `setting`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `social_login_setting`
--
ALTER TABLE `social_login_setting`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `subscription_plan`
--
ALTER TABLE `subscription_plan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `support_messages`
--
ALTER TABLE `support_messages`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `support_ticket`
--
ALTER TABLE `support_ticket`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `templates`
--
ALTER TABLE `templates`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=104;

--
-- AUTO_INCREMENT for table `transaction`
--
ALTER TABLE `transaction`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `user_chat`
--
ALTER TABLE `user_chat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

--
-- AUTO_INCREMENT for table `user_delete_request`
--
ALTER TABLE `user_delete_request`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `support_messages`
--
ALTER TABLE `support_messages`
  ADD CONSTRAINT `support_messages_user_id_foreign` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
